package it.palex.attendanceManagement.core.service.turnstile;

import it.palex.attendanceManagement.commons.utils.tables.*;
import it.palex.attendanceManagement.data.entities.core.UserAttendance;
import it.palex.attendanceManagement.data.entities.enumTypes.UserAttendanceTypeEnum;
import it.palex.attendanceManagement.library.utils.DateUtility;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

public class DailyAttendanceTableGenerator {

    private static final short LABEL_COLOR =  CellColor.AQUA.color;
    private static final String INVALID_ATTENDANCE_SEQUENCE_STR_FLAG = "INVALID";
    private static final String PLAIN_FORMAT_SHEET_TITLE = "Attendances";
    private static final String TOTAL_TIME_SHEET_TITLE = "Total Time";

    public static ArrayList<Table> createExportAttendance(String locale, List<UserAttendance> attendances){
        Table t1 = createExportPlainFormatTable(locale, attendances);
        Table t2 = createExportDurationTable(attendances);
        ArrayList<Table> tables = new ArrayList<>(2);
        tables.add(t2);
        tables.add(t1);

        return tables;
    }

    /**
     * WARNING THIS METHOD WORKS ONLY IF ATTENDANCES PASSED ARE ORDERED BY USER ID AND THEN BY DATE
     * @param attendances
     * @return
     */
    private static Table createExportDurationTable(List<UserAttendance> attendances) {
        final int START_COLUMN_INDEX = 0;

        DinamicTable table = new DinamicTable(TOTAL_TIME_SHEET_TITLE);
        table.addBlankRow();

        //add label row
        TableRowDinamic labelTableRow = new TableRowDinamic();
        int columnIndex = START_COLUMN_INDEX;

        columnIndex = addLabelRow(labelTableRow, columnIndex, "User ID");
        columnIndex = addLabelRow(labelTableRow, columnIndex, "Nome");
        columnIndex = addLabelRow(labelTableRow, columnIndex, "Cognome");
        columnIndex = addLabelRow(labelTableRow, columnIndex, "Tempo di Permanenza");

        table.addTableRow(labelTableRow);

        Iterator<UserAttendance> it = attendances.iterator();

        UserAttendance firstUserAttendance = null;

        if(it.hasNext()){
            firstUserAttendance = it.next();
        }

        while(firstUserAttendance!=null){
            TableRowDinamic row = new TableRowDinamic();
            columnIndex = START_COLUMN_INDEX;

            row.setColumnValue(columnIndex, new CellStringValue(firstUserAttendance.getUserProfile().getId()));
            columnIndex++;
            row.setColumnValue(columnIndex, new CellStringValue(firstUserAttendance.getUserProfile().getName()));
            columnIndex++;
            row.setColumnValue(columnIndex, new CellStringValue(firstUserAttendance.getUserProfile().getSurname()));
            columnIndex++;

            List<UserAttendance> attendancesOfSingleUser = new ArrayList<>(4);

            attendancesOfSingleUser.add(firstUserAttendance);
            Integer currentUserId = firstUserAttendance.getUserProfile().getId();

            boolean newUserFound = false;

            while(it.hasNext() && !newUserFound){
                UserAttendance userAttendance = it.next();

                if(currentUserId.equals(userAttendance.getUserProfile().getId())){
                    attendancesOfSingleUser.add(userAttendance);
                }else{
                   //user changed
                    newUserFound = true;
                    firstUserAttendance = userAttendance;
                }

            }//while

            String totalTimeDescription = calculateUserAttendanceTotalTime(attendancesOfSingleUser);
            row.setColumnValue(columnIndex, new CellStringValue(totalTimeDescription));
            columnIndex++;


            // if no new user was found
            if(!newUserFound){
                firstUserAttendance = null;
            }

            table.addTableRow(row);
        }//while

        return table;
    }


    private static String calculateUserAttendanceTotalTime(List<UserAttendance> attendancesOfSingleUser){
        int totalTimeSeconds = 0;

        Iterator<UserAttendance> it = attendancesOfSingleUser.iterator();

        while(it.hasNext()){
            UserAttendance enter = it.next();

            if(!UserAttendanceTypeEnum.ENTER.name().equals(enter.getType())){
                return INVALID_ATTENDANCE_SEQUENCE_STR_FLAG;
            }

            if(!it.hasNext()){
                return INVALID_ATTENDANCE_SEQUENCE_STR_FLAG;
            }

            UserAttendance exit = it.next();

            if(exit.getTimestamp().before(enter.getTimestamp())){
                return INVALID_ATTENDANCE_SEQUENCE_STR_FLAG;
            }

            totalTimeSeconds = totalTimeSeconds + (int)DateUtility.diffInSeconds(exit.getTimestamp(), enter.getTimestamp());

        }

        return DateUtility.secondsToHHMMSS(totalTimeSeconds);
    }



    private static Table createExportPlainFormatTable(String locale, List<UserAttendance> attendances) {
        final int START_COLUMN_INDEX = 0;

        DinamicTable table = new DinamicTable(PLAIN_FORMAT_SHEET_TITLE);
        table.addBlankRow();

        //add label row
        TableRowDinamic labelTableRow = new TableRowDinamic();
        int columnIndex = START_COLUMN_INDEX;

        columnIndex = addLabelRow(labelTableRow, columnIndex, "User ID");
        columnIndex = addLabelRow(labelTableRow, columnIndex, "Nome");
        columnIndex = addLabelRow(labelTableRow, columnIndex, "Cognome");
        columnIndex = addLabelRow(labelTableRow, columnIndex, "Ora ("+locale+")");
        columnIndex = addLabelRow(labelTableRow, columnIndex, "Tipo");
        columnIndex = addLabelRow(labelTableRow, columnIndex, "ID Tornello");

        table.addTableRow(labelTableRow);

        for(UserAttendance attendance: attendances){
            TableRowDinamic row = new TableRowDinamic();
            columnIndex = START_COLUMN_INDEX;
            row.setColumnValue(columnIndex, new CellStringValue(attendance.getUserProfile().getId()));
            columnIndex++;
            row.setColumnValue(columnIndex, new CellStringValue(attendance.getUserProfile().getName()));
            columnIndex++;
            row.setColumnValue(columnIndex, new CellStringValue(attendance.getUserProfile().getSurname()));
            columnIndex++;


            String timestampHour = convertDateToLocalizedDate(locale, attendance.getTimestamp());


            row.setColumnValue(columnIndex, new CellStringValue(timestampHour));
            columnIndex++;
            row.setColumnValue(columnIndex, new CellStringValue(attendance.getType()));
            columnIndex++;
            row.setColumnValue(columnIndex, new CellStringValue(attendance.getTurnstile().getId()));
            columnIndex++;

            table.addTableRow(row);
        }

        return table;
    }

    public static boolean isSupportedLocale(String locale){
        if(locale==null){
            return false;
        }
        if(locale.equals("Europe/Rome") || locale.equals("UTC")){
            return true;
        }
        return false;
    }

    private static String convertDateToLocalizedDate(String locale, Date date){
        if(locale==null){
            throw new NullPointerException();
        }
        if(isSupportedLocale(locale)){
            ZoneId zone = ZoneId.of(locale);
            LocalDateTime localDateTime = date.toInstant().atZone(zone).toLocalDateTime();
            ZonedDateTime zonedDateTimeFrom = localDateTime.atZone(zone);
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");

            return zonedDateTimeFrom.format(formatter);
        }

        throw new IllegalArgumentException("Not supported locale: "+locale);
    }

    private static int addLabelRow(TableRowDinamic labelTableRow, int index, String title){
        labelTableRow.setColumnValue(index, new ColumnLabel(title, LABEL_COLOR));
        return index+1;
    }


}
