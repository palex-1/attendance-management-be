package it.palex.attendanceManagement.library.exception;

public enum StandardReturnCodesEnum {

	DEACTIVATION_DATE_CHANGE_INVALID(0, "Deactivation date is invalid"),
	DEACTIVATION_DATE_CHANGE_INVALID_MOVED_NEXT_DEACTIVATION_DATE_ERROR(1, "Change of data attivazione is not valid, remember that the deactivation date can only moved next non back"),

	PASSWORD_USED_RECENTLY(2, "This password has been used recently"),
    THIS_IS_YOUR_PASSWORD(3, "This is your current password"),
    
    INVALID_OTP_CODE(4, "invalid otp code"),
    TWO_FA_REQUIRED_CODE(5, "Two factor authentication required"),
    MUST_CHANGE_PASSWORD(6, "Must Change Password"),
    EXPIRED_OTP_CODE(7, "Expired OTP code"),
    MAX_OTP_ATTEMPT_REACHED(8, "Maximum otp code confirm attempt reached"),
    
    UNSUPPORTED_FILE_EXTENSIONS(395, "Unsupported File extension"),
    USER_NOT_FOUND(396, "User not found"),
	DEVICE_ID_OWNED_BY_ONOTHER_USER(397, "Device id is owned by onother user"),
	YOU_MUST_ACCEPT_TERMS_AND_CONDITIONS(398, "You must accept Terms and Conditions"), 
	
	INVALID_CF(399, "Invalid fiscal code"),  
	INVALID_BIRTH_DATE(400, "Invalid Birth Date"), 
	INVALID_PHONE_NUMBER(401, "Invalid Phone Number"), 
	INVALID_EMAIL(402, "Invalid Email"), 
	USERNAME_ALREADY_REGISTRED(403, "Username is already registered"), 
	MAIL_ALREADY_REGISTRED(404, "Mail address is already used"), 
	PHONE_NUMBER_ALREADY_REGISTERED(405, "Phone number is already registered"), 
	PERMISSION_GROUP_NOT_EXISTS(406, "Permission group not exists"), 
	COMPANY_NOT_EXISTS(407, "Company not exists"), 
	ERROR_SENDING_EMAIL_PLEASE_CHECK_IT(408, "Error sending email please check it"),
	USER_LEVEL_NOT_FOUND(409, "User level not found"), 
	
	SUBMISSION_FOR_THIS_DATE_IS_LOCKED(410	, "Submission for this date is locked"), 
	WORK_TASK_NOT_FOUND(411, "Work Task not found"),
	TOTAL_WORKED_HOURS_REACHES_THE_LIMIT(412, "Total Worked Hours reached the limit"),
	UPDATED_AN_ALREADY_ADDED_TASK(413, "Updated an already added task"),
	THIS_TASK_IS_NOT_EDITABLE(414, "This task is not editable"),
	USER_IS_NOT_PART_OF_THE_TEAM(415, "User is not part of the Team"), 
	THE_TASK_IS_ENABLED_FOR_ALL_USER(416, "The task is enabled for all users"),
	TASK_IN_NOT_YET_ENABLED(417, "The task is not yet enabled"),
	
	ALREADY_EXISTS_A_PAYOUT_IN_MONTH_AND_DATE(418, "Already exists a payout in this month and date"),
	FISCAL_CODE_OF_USER_IS_NOT_FOUND_IN_PAYCHECK(419, "Fiscal code of user is not found in paycheck"), 
	ALREADY_EXIST_THIS_PERSONAL_DOCUMENT_FOR_USER(420, "Already exists this personal document for user"),
	PERSONAL_DOCUMENT_IN_NOT_EDITABLE(421, "Personal document is not editable"), 
	NOT_ENOUGHT_WORKED_HOURS_TO_REQUEST_FOOD_VOUCHER(422, " Not enought worked hours to request food voucher"),
	ALREADY_ADDED_FOOD_VOUCHER_REQUEST(423, "Already added food voucher request"), 
	USER_CANNOT_DISABLE_ENABLE_HIMSELF(424, "The user cannot disable himself"),
	
	TURNSTILE_NOT_FOUND(425, "Turnstile not found"),
	INVALID_CREDENTIALS(426, "Invalid Credential"),
	TURNSTILE_IS_NOT_VIRTUAL(427, "Turnstile is not virtual"),
	TURNSTILE_NOT_ACTIVE(428, "Turnstile not active"),
	
	USER_IS_NOT_AN_EMPLOYEE(429, "User is not an employee"),
	
	UPDATED_WORK_TRANSFER_REQUEST(430, "Updated work transfer request for this day"),
	
	REPORT_CANNOT_BE_MODIFIED(431, "Report cannot be modified"),
	EXPENSE_REPORT_NOT_IN_PROCESSING_STATUS(432, "Expense report is not in processing status"),
	
	LOCK_CANNOT_BE_DELETED_ANYMORE(433, "Lock cannot be deleted anymore"),
	
	ALREADY_EXISTS_A_LOCK_FOR_THIS_YEAR_AND_MONTH(433, "Already exists a lock for this year and month"),
	
	TOO_MUCH_ABSENCE_TASK_ADDED_IN_THIS_DAY(434, "Too much absence task added in this day"),
	
	ONLY_ADMIN_CAN_ADD_ADMIN(435, "Only Admin can add Admin"),
	
	NOT_FOUND_EMPLOYMENT_OFFICE(436, "Employment Office Not found"),
	;
	
	
    private int code;
    private String mess;

    StandardReturnCodesEnum(int code, String mess) {
        this.code = code;
        this.mess = mess;
    }

    public int getCode() {
        return this.code;
    }

    public String getMess() {
        return this.mess;
    }

}
