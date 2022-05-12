package it.palex.attendanceManagement.auth.dto;

import it.palex.attendanceManagement.library.rest.dtos.DTO;

import java.util.Date;

public class TemporalRangeDTO implements DTO {

    private Date dateStart;
    private Date dateEnd;

    public Date getDateStart() {
        return dateStart;
    }

    public void setDateStart(Date dateStart) {
        this.dateStart = dateStart;
    }

    public Date getDateEnd() {
        return dateEnd;
    }

    public void setDateEnd(Date dateEnd) {
        this.dateEnd = dateEnd;
    }
}
