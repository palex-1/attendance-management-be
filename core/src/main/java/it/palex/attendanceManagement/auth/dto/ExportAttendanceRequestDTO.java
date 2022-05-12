package it.palex.attendanceManagement.auth.dto;

import it.palex.attendanceManagement.library.rest.dtos.DTO;

import java.util.Date;

public class ExportAttendanceRequestDTO implements DTO {

    private static final long serialVersionUID = 1L;

    private Date day;
    private Long turnstileId;
    private String exportLocale;

    public String getExportLocale() {
        return exportLocale;
    }

    public void setExportLocale(String exportLocale) {
        this.exportLocale = exportLocale;
    }

    public Date getDay() {
        return day;
    }

    public void setDay(Date day) {
        this.day = day;
    }

    public Long getTurnstileId() {
        return turnstileId;
    }

    public void setTurnstileId(Long turnstileId) {
        this.turnstileId = turnstileId;
    }
}
