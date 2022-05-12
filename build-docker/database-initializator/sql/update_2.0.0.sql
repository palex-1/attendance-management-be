set search_path to attendance_management;

alter table work_task add column total_budget float8 default 0 not null;

alter table user_profile_contract_info add column hourly_cost float8 default 0 not null;
alter table completed_task add column total_cost float8 default 0 not null;


INSERT INTO GLOBAL_CONFIGURATIONS(SETTING_AREA, SETTING_KEY, SETTING_VALUE, VISIBLE, EDITABLE)
VALUES( 'EXPENSES_TYPES', 'PROCUREMENT', 'Materie Prime', TRUE, TRUE);

INSERT INTO GLOBAL_CONFIGURATIONS(SETTING_AREA, SETTING_KEY, SETTING_VALUE, VISIBLE, EDITABLE)
VALUES( 'EXPENSES_TYPES', 'CONSULTING', 'Consulenze', TRUE, TRUE);

INSERT INTO GLOBAL_CONFIGURATIONS(SETTING_AREA, SETTING_KEY, SETTING_VALUE, VISIBLE, EDITABLE)
VALUES( 'EXPENSES_TYPES', 'OTHER', 'Altro', TRUE, TRUE);



CREATE TABLE task_expenses(
    ID bigserial NOT NULL,
	task_id int8 NOT NULL,
	title varchar(255) not null,
	description varchar(2000) not null,
	expense_type varchar(255) not null,
	day DATE NOT NULL,
	amount float not null,
	
    primary key(ID),
	FOREIGN KEY (task_id) REFERENCES work_task (id) ON UPDATE CASCADE
);

/*MAKE TABLE completed_task*/
ALTER TABLE task_expenses
  ADD CREATED_BY VARCHAR(512),
  ADD CREATED_DATE TIMESTAMP,
  ADD LAST_MODIFIED_BY VARCHAR(512),
  ADD LAST_MODIFIED_DATE TIMESTAMP;


alter table expense_report add column task_id int8;
ALTER TABLE expense_report ADD FOREIGN KEY (task_id) REFERENCES work_task(id) ON UPDATE CASCADE;


INSERT INTO PERMISSIONS(AUTHORITY) VALUES ('TASK_EXPENSES_READ');
INSERT INTO PERMISSIONS(AUTHORITY) VALUES ('TASK_EXPENSES_UPDATE');
INSERT INTO PERMISSIONS(AUTHORITY) VALUES ('TASK_EXPENSES_CREATE');
INSERT INTO PERMISSIONS(AUTHORITY) VALUES ('TASK_EXPENSES_DELETE');


INSERT INTO PERMISSION_GROUP(NAME, FK_AUTHORITY) VALUES ('ADMINISTRATION', 'TASK_EXPENSES_READ');
INSERT INTO PERMISSION_GROUP(NAME, FK_AUTHORITY) VALUES ('ADMINISTRATION', 'TASK_EXPENSES_CREATE');
INSERT INTO PERMISSION_GROUP(NAME, FK_AUTHORITY) VALUES ('ADMINISTRATION', 'TASK_EXPENSES_UPDATE');
INSERT INTO PERMISSION_GROUP(NAME, FK_AUTHORITY) VALUES ('ADMINISTRATION', 'TASK_EXPENSES_DELETE');

INSERT INTO PERMISSION_GROUP(NAME, FK_AUTHORITY) VALUES ('HR_BUSINESS_PARTNER', 'TASK_EXPENSES_READ');
INSERT INTO PERMISSION_GROUP(NAME, FK_AUTHORITY) VALUES ('HR_BUSINESS_PARTNER', 'TASK_EXPENSES_CREATE');
INSERT INTO PERMISSION_GROUP(NAME, FK_AUTHORITY) VALUES ('HR_BUSINESS_PARTNER', 'TASK_EXPENSES_UPDATE');
INSERT INTO PERMISSION_GROUP(NAME, FK_AUTHORITY) VALUES ('HR_BUSINESS_PARTNER', 'TASK_EXPENSES_DELETE');

INSERT INTO AUTHORITIES( FK_ID_USERS_AUTH_DETAILS, AUTHORITY)
select id as FK_ID_USERS_AUTH_DETAILS, 'TASK_EXPENSES_READ' from USERS_AUTH_DETAILS where
	permission_group_name='ADMINISTRATION' or permission_group_name='HR_BUSINESS_PARTNER';

INSERT INTO AUTHORITIES( FK_ID_USERS_AUTH_DETAILS, AUTHORITY)
select id as FK_ID_USERS_AUTH_DETAILS, 'TASK_EXPENSES_CREATE' from USERS_AUTH_DETAILS where
	permission_group_name='ADMINISTRATION' or permission_group_name='HR_BUSINESS_PARTNER';

	INSERT INTO AUTHORITIES( FK_ID_USERS_AUTH_DETAILS, AUTHORITY)
select id as FK_ID_USERS_AUTH_DETAILS, 'TASK_EXPENSES_UPDATE' from USERS_AUTH_DETAILS where
	permission_group_name='ADMINISTRATION' or permission_group_name='HR_BUSINESS_PARTNER';

	INSERT INTO AUTHORITIES( FK_ID_USERS_AUTH_DETAILS, AUTHORITY)
select id as FK_ID_USERS_AUTH_DETAILS, 'TASK_EXPENSES_DELETE' from USERS_AUTH_DETAILS where
	permission_group_name='ADMINISTRATION' or permission_group_name='HR_BUSINESS_PARTNER';




INSERT INTO PERMISSIONS(AUTHORITY) VALUES ('BUDGET_SUMMARY_CREATE');

INSERT INTO PERMISSION_GROUP(NAME, FK_AUTHORITY) VALUES ('ADMINISTRATION', 'BUDGET_SUMMARY_CREATE');

INSERT INTO PERMISSION_GROUP(NAME, FK_AUTHORITY) VALUES ('HR_BUSINESS_PARTNER', 'BUDGET_SUMMARY_CREATE');


INSERT INTO AUTHORITIES( FK_ID_USERS_AUTH_DETAILS, AUTHORITY)
select id as FK_ID_USERS_AUTH_DETAILS, 'BUDGET_SUMMARY_CREATE' from USERS_AUTH_DETAILS where
	permission_group_name='ADMINISTRATION' or permission_group_name='HR_BUSINESS_PARTNER';


alter table GLOBAL_CONFIGURATIONS add column is_secret boolean not null default false;

update GLOBAL_CONFIGURATIONS set is_secret=true
 where SETTING_AREA='PROFILE_SMTP' and SETTING_KEY='MAIL_SMTP_PASSWORD';

update GLOBAL_CONFIGURATIONS set is_secret=true
 where SETTING_AREA='PROFILE_TWILIO' and SETTING_KEY='AUTH_TOKEN';


INSERT INTO GLOBAL_CONFIGURATIONS(SETTING_AREA, SETTING_KEY, SETTING_VALUE, VISIBLE, EDITABLE)
 VALUES( 'EMAIL_SENDER', 'TYPE', 'SMTP', TRUE, TRUE);



update database_version set VERSION ='2.0.0' where id=1;