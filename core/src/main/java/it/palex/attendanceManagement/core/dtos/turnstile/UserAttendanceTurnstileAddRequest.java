package it.palex.attendanceManagement.core.dtos.turnstile;

import java.util.Date;

public class UserAttendanceTurnstileAddRequest {
    private String type;
    private Date timestamp;
    private Integer userProfileId;


    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }
    public Integer getUserProfileId() {
        return userProfileId;
    }

    public void setUserProfileId(Integer userProfileId) {
        this.userProfileId = userProfileId;
    }

}
