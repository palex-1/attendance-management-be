set search_path to attendance_management;

alter table turnstile add column AUTH_TOKEN varchar(64);
alter table completed_task add column ACTIVITY_DESCRIPTION varchar(255);

update database_version set VERSION ='1.1.0' where id=1;