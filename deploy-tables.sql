CREATE SCHEMA attendance_management;

set search_path to attendance_management;


create table permission_group_label(
		id  serial not null,
        name varchar(50) not null,
        
        primary key (id),
	    unique(name)
);

CREATE UNIQUE INDEX permission_group_label_INSENSITIVE_CONSTRAINT on 
permission_group_label (lower(name));

create table users_auth_details (
       id  serial not null,
       username varchar(255) not null,
       hashed_password varchar(128),
       ISACCOUNTNONEXPIRED boolean not null,
       ISACCOUNTNONLOCKED boolean not null,
       ISCREDENTIALSNONEXPIRED boolean not null,
       ISENABLED boolean not null,
       last_password_change_date timestamp not null,
       two_fa_enabled boolean not null,
       must_reset_password boolean not null default false,
       registered_with varchar(20) not null default 'LOCAL',
       permission_group_name varchar(50) not null,
       
       primary key (id),
       unique(username),
       foreign key (permission_group_name) REFERENCES permission_group_label(name) ON UPDATE CASCADE
);


CREATE UNIQUE INDEX USERNAME_INSENSITIVE_CONSTRAINT on 
users_auth_details (lower(username));

  
ALTER TABLE users_auth_details
  ADD CREATED_BY VARCHAR(512),
  ADD CREATED_DATE TIMESTAMP default ((now() at time zone 'utc')),
  ADD LAST_MODIFIED_BY VARCHAR(512),
  ADD LAST_MODIFIED_DATE TIMESTAMP;
  
  

create table permissions (
    id  serial not null,
    authority varchar(50) not null,

    primary key (id),
	unique(authority)
);

CREATE UNIQUE INDEX PERMISSION_AUTHORITY_INSENSITIVE_CONSTRAINT on 
 PERMISSIONS (lower(AUTHORITY));



 
 
create table permission_group (
        id  serial not null,
        name varchar(50) not null,
        fk_authority varchar(50) not null,

        primary key (id),
	    unique(name, FK_AUTHORITY),
	    foreign key (name) REFERENCES permission_group_label(name) ON UPDATE CASCADE,
	    
		foreign key (FK_AUTHORITY) REFERENCES PERMISSIONS(AUTHORITY) ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE UNIQUE INDEX PERMISSION_GROUP_INSENSITIVE_CONSTRAINT on 
PERMISSION_GROUP (lower(NAME), lower(FK_AUTHORITY));


create table authorities (
        id  serial not null,
        AUTHORITY VARCHAR(50) NOT NULL,
        FK_ID_USERS_AUTH_DETAILS int4 not null,

		FOREIGN KEY (AUTHORITY) REFERENCES PERMISSIONS(AUTHORITY) ON DELETE CASCADE ON UPDATE CASCADE,
	    FOREIGN KEY (FK_ID_USERS_AUTH_DETAILS) REFERENCES USERS_AUTH_DETAILS(ID) ON DELETE CASCADE ON UPDATE CASCADE,
        unique (FK_ID_USERS_AUTH_DETAILS, AUTHORITY),
        primary key (id)
);


create table user_password_change_history (
        fk_id_users_auth_details int4 not null,
        hashed_password varchar(255) not null,
        password_change_date timestamp not null,

        FOREIGN KEY (FK_ID_USERS_AUTH_DETAILS) REFERENCES USERS_AUTH_DETAILS (ID) ON DELETE CASCADE ON UPDATE CASCADE,
        primary key (fk_id_users_auth_details, hashed_password, password_change_date)
);



create table failed_login_attempt (
    ip varchar(255) not null,
    login_date timestamp not null,
    user_agent varchar(255) not null,
    username varchar(255) not null,

    primary key (ip, login_date, user_agent, username)
);



create table reset_password_request (
        creation_date timestamp not null,
        expiration_date timestamp,
        ip varchar(39) not null,
        user_agent varchar(512) not null,
        username varchar(255) not null,
        request_token varchar(128) not null,
        reset_email_sent_successfully boolean,

        UNIQUE (REQUEST_TOKEN),
        primary key (creation_date, ip, user_agent, username)
);



create table successfully_login_logs (
        fk_id_users_auth_details int4 not null,
        ip varchar(39) not null,
        login_date timestamp not null,
        user_agent varchar(512) not null,

        primary key (fk_id_users_auth_details, ip, login_date, user_agent),
        FOREIGN KEY (FK_ID_USERS_AUTH_DETAILS) REFERENCES USERS_AUTH_DETAILS (ID) ON DELETE CASCADE ON UPDATE CASCADE

);


CREATE TABLE USERS_ACCESS_TOKEN (
    FK_ID_USERS_AUTH_DETAILS INTEGER NOT NULL,
    TOKEN VARCHAR(4096) NOT NULL,
    REFRESH_TOKEN VARCHAR(128),
    DEVICE_IDENTIFIER VARCHAR(64),
    ISSUED_DATE TIMESTAMP NOT NULL,
    EXPIRATION_DATE TIMESTAMP NOT NULL,
    TWO_FACTOR_AUTHENTICATION_IN_PROGRESS BOOLEAN NOT NULL,
    ONE_TIME_PASSWORD VARCHAR(16),
	ONE_TIME_PASSWORD_EXPIRES TIMESTAMP,
	must_reset_password BOOLEAN NOT NULL default false,
    ONE_TIME_PASSWORD_REQ_NUMBER int4 not null default 0,
    
	primary key(TOKEN),
    FOREIGN KEY (FK_ID_USERS_AUTH_DETAILS) REFERENCES USERS_AUTH_DETAILS (ID) ON DELETE CASCADE ON UPDATE CASCADE
);


 create table user_contact_type (
       id  serial not null,
       c_type varchar(30) not null,

       primary key (id),
	   unique(c_type)
);

CREATE UNIQUE INDEX USER_CONTACT_TYPE_INSENSITIVE_CONSTRAINT on 
    USER_CONTACT_TYPE (lower(C_TYPE));


CREATE TABLE USER_CONTACTS(
            ID SERIAL NOT NULL,
            USER_CONTACT_TYPE VARCHAR(30) NOT NULL,
            C_VALUE VARCHAR(255) NOT NULL,
            VERIFIED BOOLEAN NOT NULL,
            FK_ID_USERS_AUTH_DETAILS INTEGER NOT NULL,
            verification_token varchar(1024),
            verification_token_creation_date timestamp,
            verification_token_expiration_date timestamp,
            
            PRIMARY KEY(ID),
            UNIQUE(USER_CONTACT_TYPE, FK_ID_USERS_AUTH_DETAILS),
            --unique(USER_CONTACT_TYPE, C_VALUE),

            FOREIGN KEY (FK_ID_USERS_AUTH_DETAILS) REFERENCES USERS_AUTH_DETAILS (ID) ON DELETE CASCADE ON UPDATE CASCADE,
            FOREIGN KEY (USER_CONTACT_TYPE) REFERENCES USER_CONTACT_TYPE (C_TYPE) ON UPDATE CASCADE
);

CREATE UNIQUE INDEX USER_CONTACT_type_and_value_INSENSITIVE_CONSTRAINT on 
    USER_CONTACTS (lower(USER_CONTACT_TYPE), lower(C_VALUE));

    
create table supported_lang_i18n (
        id  serial not null,
        lang varchar(10) not null,
		
        primary key (id),
		unique(lang)
);

CREATE UNIQUE INDEX SUPPORTED_LANG_I18N_INSENSITIVE_CONSTRAINT on 
    SUPPORTED_LANG_I18N (lower(LANG));


create table document (
        id  bigserial not null,
        description varchar(500),
        file_manager varchar(50) not null,
        file_path varchar(4096) not null,
        full_file_name varchar(255) not null,
		file_size int8 not null,
        
        primary key (id)
);


/*MAKE TABLE AUDIBLE*/
ALTER TABLE document
  ADD CREATED_BY VARCHAR(512),
  ADD CREATED_DATE TIMESTAMP default ((now() at time zone 'utc')),
  ADD LAST_MODIFIED_BY VARCHAR(512),
  ADD LAST_MODIFIED_DATE TIMESTAMP;


create table global_configurations (
        id  serial not null,
        editable boolean not null,
        setting_area varchar(50) not null,
        setting_key varchar(50) not null,
        setting_value text,
        visible boolean not null,

        primary key (id),
        UNIQUE(SETTING_AREA, SETTING_KEY)
);

CREATE UNIQUE INDEX GLOBAL_CONFIGURATIONS_INSENSITIVE_CONSTRAINT on 
    GLOBAL_CONFIGURATIONS (lower(SETTING_AREA), lower(SETTING_KEY));

ALTER TABLE global_configurations
  ADD CREATED_BY VARCHAR(512),
  ADD CREATED_DATE TIMESTAMP default ((now() at time zone 'utc')),
  ADD LAST_MODIFIED_BY VARCHAR(512),
  ADD LAST_MODIFIED_DATE TIMESTAMP;
  
  
create table message_type (
        id  serial not null,
        m_type varchar(50) not null,

        unique(m_type),
        primary key (id)
);


CREATE UNIQUE INDEX MESSAGE_TYPE_INSENSITIVE_CONSTRAINT on 
    MESSAGE_TYPE (lower(M_TYPE));



create table message_template (
        id  serial not null,
        message TEXT not null,
        subject varchar(997) not null,
        lang varchar(10) not null,
        m_type varchar(255) not null,

        primary key (id),
        UNIQUE(M_TYPE, LANG),
	    FOREIGN KEY (M_TYPE) REFERENCES MESSAGE_TYPE (M_TYPE) ON DELETE CASCADE ON UPDATE CASCADE,
	    FOREIGN KEY (LANG) REFERENCES SUPPORTED_LANG_I18N (LANG) ON DELETE CASCADE ON UPDATE CASCADE
);    
    
    
CREATE TABLE TICKET_DOWNLOAD (
	ID BIGSERIAL NOT NULL,
    document int8 NOT NULL,
    token_download VARCHAR(256) NOT NULL,
    creation_date TIMESTAMP NOT NULL,
    expiration_date TIMESTAMP NOT NULL,
    is_one_shot boolean not null default false,
    
    PRIMARY KEY(ID),
	FOREIGN KEY (document) REFERENCES DOCUMENT (ID) ON DELETE CASCADE ON UPDATE CASCADE,
    UNIQUE(TOKEN_DOWNLOAD)
);
    
ALTER TABLE TICKET_DOWNLOAD
  ADD CREATED_BY VARCHAR(512),
  ADD CREATED_DATE TIMESTAMP default ((now() at time zone 'utc')),
  ADD LAST_MODIFIED_BY VARCHAR(512),
  ADD LAST_MODIFIED_DATE TIMESTAMP;    
    
    
    
    
    
/*non iserire la foreign key altrimenti gli attacchi a forza bruta non vengono loggati*/
CREATE TABLE RESET_USERNAME_REQUEST(
				IP VARCHAR(39) NOT NULL, 
				USER_AGENT VARCHAR(512) NOT NULL,
				CREATION_DATE TIMESTAMP NOT NULL,
				HASHED_USERNAME VARCHAR(64) NOT NULL,
				NEW_HASHED_USERNAME VARCHAR(64) NOT NULL,
				REQUEST_TOKEN VARCHAR(128) NOT NULL,
				RESET_EMAIL_SENT_SUCCESSFULLY BOOLEAN NOT NULL,

				UNIQUE (REQUEST_TOKEN),
				PRIMARY KEY (IP, USER_AGENT, CREATION_DATE, HASHED_USERNAME)
);


     
CREATE TABLE USER_INVITATIONS(
		ID SERIAL NOT NULL,
		NAME VARCHAR(20) NOT NULL,
		SURNAME VARCHAR(20) NOT NULL,
		EMAIL VARCHAR(255) NOT NULL,
		TOKEN VARCHAR(128) NOT NULL,
                
		PRIMARY KEY (ID),
        UNIQUE(TOKEN),
        UNIQUE(EMAIL)
);


CREATE TABLE COMPANY_BRANCH_TYPE(
	ID SERIAL NOT NULL,
	TYPE VARCHAR(50) not null,
	
	PRIMARY KEY(ID),
	UNIQUE(TYPE)
);

INSERT INTO COMPANY_BRANCH_TYPE(TYPE) VALUES ('COMPANY_OFFICE'), ('EXTERNAL_OFFICE');


CREATE TABLE OFFICE(
	ID SERIAL NOT NULL,
	OFFICE_NAME VARCHAR(200) NOT NULL,
    STREET VARCHAR(200) NOT NULL,
    CITY VARCHAR(200) NOT NULL,
    PROVINCE VARCHAR(200) NOT NULL,
    NATION VARCHAR(100) NOT NULL,
    ZIP_CODE VARCHAR(10) NOT NULL,
    COMPANY_BRANCH_TYPE VARCHAR(20) NOT NULL,

	PRIMARY KEY (ID),
	UNIQUE(OFFICE_NAME),
	FOREIGN KEY (COMPANY_BRANCH_TYPE) REFERENCES COMPANY_BRANCH_TYPE (TYPE) ON UPDATE CASCADE

);

CREATE UNIQUE INDEX OFFICE_INSENSITIVE_CONSTRAINT on 
OFFICE (lower(OFFICE_NAME));

/*MAKE TABLE AUDIBLE*/
ALTER TABLE OFFICE
  ADD CREATED_BY VARCHAR(512),
  ADD CREATED_DATE TIMESTAMP,
  ADD LAST_MODIFIED_BY VARCHAR(512),
  ADD LAST_MODIFIED_DATE TIMESTAMP;

  

  

CREATE TABLE user_level(
	ID SERIAL NOT NULL,
	level VARCHAR(50) not null,
	monthly_vacation_days float not null,
	monthly_leave_hours float not null,
	bank_hour_enabled boolean not null,
	extra_work_paid boolean not null,
	
	PRIMARY KEY(ID),
	UNIQUE(level)
);


/*MAKE TABLE AUDIBLE*/
ALTER TABLE user_level
  ADD CREATED_BY VARCHAR(512),
  ADD CREATED_DATE TIMESTAMP,
  ADD LAST_MODIFIED_BY VARCHAR(512),
  ADD LAST_MODIFIED_DATE TIMESTAMP;

  
CREATE TABLE COMPANY(
	ID SERIAL NOT NULL,
	NAME VARCHAR(50) NOT NULL,
	DESCRIPTION VARCHAR(500) NOT NULL,
	IS_ROOT BOOLEAN DEFAULT FALSE NOT NULL,
	
	PRIMARY KEY(ID),
	UNIQUE(NAME)
);

/*MAKE TABLE AUDIBLE*/
ALTER TABLE COMPANY
  ADD CREATED_BY VARCHAR(512),
  ADD CREATED_DATE TIMESTAMP,
  ADD LAST_MODIFIED_BY VARCHAR(512),
  ADD LAST_MODIFIED_DATE TIMESTAMP;

create table user_profile (
        id int4 not null,
        birth_date date not null,
        name varchar(50) not null,
        sex varchar(1) not null,
        surname varchar(50) not null,
        supported_lang_i18n int4,
		terms_and_condition_accepted boolean not null default true,
		fiscal_code VARCHAR(30),
		date_of_employment date,
		company int4,
		email varchar(255), -- the length must be the same of user_contacts.c_value
		phone_number varchar(255),-- the length must be the same of user_contacts.c_value
				
        primary key (id),
        FOREIGN KEY (company) REFERENCES company (id) ON DELETE set null ON UPDATE CASCADE,
        FOREIGN KEY (id) REFERENCES users_auth_details (id) ON DELETE CASCADE ON UPDATE CASCADE,
        FOREIGN KEY (supported_lang_i18n) REFERENCES supported_lang_i18n (id)  on update cascade ON DELETE SET NULL
);

/*MAKE TABLE AUDIBLE*/
ALTER TABLE USER_PROFILE
  ADD CREATED_BY VARCHAR(512),
  ADD CREATED_DATE TIMESTAMP,
  ADD LAST_MODIFIED_BY VARCHAR(512),
  ADD LAST_MODIFIED_DATE TIMESTAMP;


  
create table user_profile_contract_info(
	id serial not null,
	work_day_hours int4 not null,
	level int4 not null,
	user_profile INTEGER NOT NULL,
	residual_vacation_days float not null default 0,
	residual_leave_hours float not null default 0,
	employment_office int4,
	
	primary key (id),
	unique(user_profile),
	FOREIGN KEY (employment_office) REFERENCES OFFICE (id) on delete set null on update cascade,
	FOREIGN KEY (level) REFERENCES user_level (id)  on update cascade,
	FOREIGN KEY (user_profile) REFERENCES user_profile (id) ON DELETE CASCADE ON UPDATE CASCADE
);
  
/*MAKE TABLE AUDIBLE*/
ALTER TABLE user_profile_contract_info
  ADD CREATED_BY VARCHAR(512),
  ADD CREATED_DATE TIMESTAMP,
  ADD LAST_MODIFIED_BY VARCHAR(512),
  ADD LAST_MODIFIED_DATE TIMESTAMP;

  

CREATE TABLE CHANGE_EMAIL_REQUEST(
			user_profile INTEGER NOT NULL,
			IP VARCHAR(39) NOT NULL, 
			USER_AGENT VARCHAR(512) NOT NULL,
			CREATION_DATE TIMESTAMP NOT NULL,
			NEW_EMAIL VARCHAR(255) NOT NULL,
			TOKEN VARCHAR(128) NOT NULL,

			PRIMARY KEY(TOKEN),
			FOREIGN KEY (user_profile) REFERENCES user_profile (id) ON DELETE CASCADE ON UPDATE CASCADE
);

/*Tipo residenza, domicilio*/
CREATE TABLE ADDRESS_TYPE(
	ID serial NOT NULL,
	TYPE VARCHAR(50) NOT NULL,

	PRIMARY KEY(ID),
	UNIQUE(TYPE)
);

INSERT INTO address_type(type) VALUES ('RESIDENCE'), ('DOMICILE');


CREATE TABLE USER_PROFILE_ADDRESS(
	ID bigserial NOT NULL,
	USER_PROFILE INTEGER NOT NULL,
	ADDRESS_TYPE VARCHAR(255) NOT NULL,
    STREET VARCHAR(255) NOT NULL,
    CITY VARCHAR(255) NOT NULL,
    PROVINCE VARCHAR(255) NOT NULL,
    NATION VARCHAR(255) NOT NULL,
    ZIP_CODE VARCHAR(10) NOT NULL,
    
    PRIMARY KEY(ID),
	UNIQUE(USER_PROFILE, ADDRESS_TYPE),
	
    FOREIGN KEY (ADDRESS_TYPE) REFERENCES ADDRESS_TYPE (TYPE) ON DELETE CASCADE ON UPDATE CASCADE,
	FOREIGN KEY (USER_PROFILE) REFERENCES USER_PROFILE (id) ON DELETE CASCADE ON UPDATE CASCADE
);

/*MAKE TABLE AUDIBLE*/
ALTER TABLE USER_PROFILE_ADDRESS
  ADD CREATED_BY VARCHAR(512),
  ADD CREATED_DATE TIMESTAMP,
  ADD LAST_MODIFIED_BY VARCHAR(512),
  ADD LAST_MODIFIED_DATE TIMESTAMP;
  
  

CREATE TABLE work_task(
	id bigserial not null,
	task_code VARCHAR(25) NOT NULL,
	task_description VARCHAR(1000) NOT NULL,
	client_vat_num VARCHAR(20),
	billable BOOLEAN NOT NULL,
	activation_date TIMESTAMP NOT NULL,
	deactivation_date TIMESTAMP,
	is_enabled_for_all_user boolean not null default false,
	is_absence_task boolean not null default false,
	
	primary key(id),
	unique(task_code)
);

/*MAKE TABLE work_task*/
ALTER TABLE work_task
  ADD CREATED_BY VARCHAR(512),
  ADD CREATED_DATE TIMESTAMP,
  ADD LAST_MODIFIED_BY VARCHAR(512),
  ADD LAST_MODIFIED_DATE TIMESTAMP;

CREATE UNIQUE INDEX task_code_INSENSITIVE_CONSTRAINT on 
work_task (lower(task_code));
 

CREATE TABLE completed_task(
    ID bigserial NOT NULL,
	task_code VARCHAR(25) NOT NULL,
	worked_hours SMALLINT NOT NULL,
	day DATE NOT NULL,
	smartworked BOOLEAN NOT NULL,
	user_profile INTEGER NOT NULL,
	editable boolean not null,
	
    primary key(ID),
	FOREIGN KEY (task_code) REFERENCES work_task (task_code) ON UPDATE CASCADE,
	FOREIGN KEY (user_profile) REFERENCES user_profile (id) ON DELETE CASCADE ON UPDATE CASCADE
);

/*MAKE TABLE completed_task*/
ALTER TABLE completed_task
  ADD CREATED_BY VARCHAR(512),
  ADD CREATED_DATE TIMESTAMP,
  ADD LAST_MODIFIED_BY VARCHAR(512),
  ADD LAST_MODIFIED_DATE TIMESTAMP;
  
  
  
CREATE TABLE task_completions_locks_status(
	id serial not null,
	status varchar(30) not null,
	 
	primary key(id),
    unique(status)
);

CREATE UNIQUE INDEX task_completions_locks_status_INSENSITIVE_CONSTRAINT on 
task_completions_locks_status (lower(status));

insert into task_completions_locks_status(status) values ('NOT_TO_BE_PROCESSED'), ('TO_BE_PROCESSED'), ('PROCESSING'), ('PROCESSED');


create table task_completions_locks(
	id serial not null,
	year int4 not null,
	month int4 not null,
	hours_calculation_execution_requested boolean not null,
	status varchar(30) not null,
	processed_on_date TIMESTAMP,
	
	primary key(id),
	unique(year, month),
	FOREIGN KEY (status) REFERENCES task_completions_locks_status (status) ON DELETE CASCADE ON UPDATE CASCADE
);

ALTER TABLE task_completions_locks
  ADD CREATED_BY VARCHAR(512),
  ADD CREATED_DATE TIMESTAMP,
  ADD LAST_MODIFIED_BY VARCHAR(512),
  ADD LAST_MODIFIED_DATE TIMESTAMP;


  
CREATE TABLE team_role(
	ID SERIAL NOT NULL,
	ROLE VARCHAR(50) NOT NULL,
	
	PRIMARY KEY(ID),
	UNIQUE(ROLE)
);

CREATE UNIQUE INDEX TEAM_ROLE_INSENSITIVE_CONSTRAINT on 
TEAM_ROLE (lower(ROLE));

INSERT INTO TEAM_ROLE(ROLE) VALUES ('PROJECT_MANAGER'), ('DELIVERY_MANAGER'), 
	('ACCOUNT_MANAGER'), ('QA_REVIEWER'), ('RUOLO_GENERICO');

	
CREATE TABLE team_component_task(
	ID SERIAL NOT NULL,
	task_code VARCHAR(25) NOT NULL,
	user_profile INTEGER NOT NULL,
	TEAM_ROLE INTEGER,
	deleted boolean not null default false,
	
	primary key(ID),
	unique(task_code, user_profile),
	FOREIGN KEY (TEAM_ROLE) REFERENCES TEAM_ROLE (ID) ON UPDATE CASCADE ON DELETE SET NULL,
	FOREIGN KEY (task_code) REFERENCES work_task (task_code) ON DELETE CASCADE ON UPDATE CASCADE,
	FOREIGN KEY (user_profile) REFERENCES user_profile (id) ON DELETE CASCADE ON UPDATE CASCADE
	
);

ALTER TABLE team_component_task
  ADD CREATED_BY VARCHAR(512),
  ADD CREATED_DATE TIMESTAMP default ((now() at time zone 'utc')),
  ADD LAST_MODIFIED_BY VARCHAR(512),
  ADD LAST_MODIFIED_DATE TIMESTAMP; 
  
  
  
CREATE TABLE food_voucher_request(
	ID bigserial NOT NULL,
	USER_PROFILE INTEGER NOT NULL,
	DAY DATE NOT NULL,
	quantity int4 not null default 1,
	editable boolean not null,

	PRIMARY KEY (ID),
	FOREIGN KEY (USER_PROFILE) REFERENCES USER_PROFILE (id) ON DELETE CASCADE ON UPDATE CASCADE,
	UNIQUE (USER_PROFILE, DAY)
);


ALTER TABLE food_voucher_request
  ADD CREATED_BY VARCHAR(512),
  ADD CREATED_DATE TIMESTAMP default ((now() at time zone 'utc')),
  ADD LAST_MODIFIED_BY VARCHAR(512),
  ADD LAST_MODIFIED_DATE TIMESTAMP; 
  
  
CREATE TABLE USER_PROFILE_SETTING (
	id bigserial not null,
	setting_area varchar(50) not null,
    setting_key varchar(50) not null,
    setting_value varchar(1000) not null,
    EDITABLE boolean not null default true,
	user_profile int4 not null,

    primary key (id),
    UNIQUE(user_profile, SETTING_AREA, SETTING_KEY),
    FOREIGN KEY (user_profile) REFERENCES user_profile (ID) ON DELETE CASCADE ON UPDATE CASCADE
);




create table supported_providers(
	id serial not null,
	name varchar(50) not null,
	
	primary key(id),
	unique(name)
);

CREATE UNIQUE INDEX supported_providers_NAME_INSENSITIVE_CONSTRAINT on 
supported_providers (lower(name));


CREATE TABLE FCM_USER_TOKEN (
	id  bigserial not null,
    user_id INTEGER NOT NULL,
    TOKEN VARCHAR(4096) NOT NULL,
	device_id VARCHAR(4096) NOT NULL,
	provider_name varchar(50) not null,
	
	primary key(id),
	unique(user_id,TOKEN,provider_name),
	unique(TOKEN,provider_name),
	unique(device_id),
	FOREIGN KEY (user_id) REFERENCES users_auth_details (id) ON DELETE CASCADE ON UPDATE CASCADE,
	FOREIGN KEY (provider_name) REFERENCES supported_providers (name) ON DELETE CASCADE ON UPDATE CASCADE
);

ALTER TABLE FCM_USER_TOKEN
  ADD CREATED_BY VARCHAR(512),
  ADD CREATED_DATE TIMESTAMP default ((now() at time zone 'utc')),
  ADD LAST_MODIFIED_BY VARCHAR(512),
  ADD LAST_MODIFIED_DATE TIMESTAMP;
  
CREATE INDEX IDX_USER_ID ON FCM_USER_TOKEN(user_id);


create table user_notification(
	id bigserial not null,
	title varchar(256),
	text varchar(4096),
	user_profile integer not null,
	landing_page varchar(255),
	target_id varchar(255),
	target_sub_id varchar(255),
	
	primary key(id),
	FOREIGN KEY (user_profile) REFERENCES user_profile (ID) ON DELETE CASCADE ON UPDATE CASCADE
);

ALTER TABLE user_notification
  ADD CREATED_BY VARCHAR(512),
  ADD CREATED_DATE TIMESTAMP default ((now() at time zone 'utc')),
  ADD LAST_MODIFIED_BY VARCHAR(512),
  ADD LAST_MODIFIED_DATE TIMESTAMP;



CREATE INDEX user_notification_USER_ID ON user_notification(user_profile);

  


create table user_profile_image (
        id  serial not null,
        profile_image_id int8 not null,
        user_profile int4 not null,
        image_compression varchar(30) not null default 'NORMAL',
        download_token varchar(128) not null,
        download_token_creation_date timestamp not null default now(),
        
        
        unique(user_profile, profile_image_id),
        primary key (id),
        unique(download_token),
        FOREIGN KEY (user_profile) REFERENCES user_profile (ID) ON DELETE CASCADE ON UPDATE CASCADE,
        FOREIGN KEY (profile_image_id) REFERENCES document (id) ON DELETE CASCADE ON UPDATE CASCADE
);

ALTER TABLE user_profile_image
  ADD CREATED_BY VARCHAR(512),
  ADD CREATED_DATE TIMESTAMP default ((now() at time zone 'utc')),
  ADD LAST_MODIFIED_BY VARCHAR(512),
  ADD LAST_MODIFIED_DATE TIMESTAMP;  
  
  
  
  
create table paycheck(
	ID BIGSERIAL NOT NULL,
	user_profile int4 not null,
	document int8 not null,
	title varchar(200),
	send_email_date timestamp,
	year int4 not null,
	month int4 not null,

	primary key(id),
	FOREIGN KEY (user_profile) REFERENCES user_profile (ID) ON DELETE CASCADE ON UPDATE CASCADE,
	FOREIGN KEY (document) REFERENCES document (ID) ON DELETE cascade ON UPDATE CASCADE
);

ALTER TABLE paycheck
  ADD CREATED_BY VARCHAR(512),
  ADD CREATED_DATE TIMESTAMP default ((now() at time zone 'utc')),
  ADD LAST_MODIFIED_BY VARCHAR(512),
  ADD LAST_MODIFIED_DATE TIMESTAMP; 
  
   
  
create table personal_document_type(
	id serial not null,
	type varchar(200) not null,
	extensions_supported varchar(1000) not null,
	
	primary key(id),
	unique(type)
);
  
CREATE UNIQUE INDEX personal_document_type_INSENSITIVE_CONSTRAINT on 
personal_document_type (lower(type));
  
CREATE TABLE personal_document(
	ID BIGSERIAL NOT NULL,
	user_profile int4 not null,
	document int8 not null,
	personal_document_type int4 NOT NULL,
	upload_date TIMESTAMP NOT NULL,
	editable boolean not null default true,
	
	PRIMARY KEY(ID),
	UNIQUE(document),
	unique(personal_document_type, user_profile),
	foreign key (personal_document_type) REFERENCES personal_document_type (id) ON UPDATE CASCADE,
	FOREIGN KEY (user_profile) REFERENCES user_profile (id) ON DELETE CASCADE ON UPDATE CASCADE,
	FOREIGN KEY (document) REFERENCES document (ID) ON DELETE cascade ON UPDATE CASCADE
);
 
ALTER TABLE personal_document
  ADD CREATED_BY VARCHAR(512),
  ADD CREATED_DATE TIMESTAMP default ((now() at time zone 'utc')),
  ADD LAST_MODIFIED_BY VARCHAR(512),
  ADD LAST_MODIFIED_DATE TIMESTAMP; 
  
  
  
  
  
  
create table turnstile(
	id bigserial not null,
	title varchar(100) not null,
	description varchar(1000) not null,
	position varchar(200) not null,
	deactivated boolean not null,
	type varchar(20) not null,
	
	primary key(id)
);

ALTER TABLE turnstile
  ADD CREATED_BY VARCHAR(512),
  ADD CREATED_DATE TIMESTAMP default ((now() at time zone 'utc')),
  ADD LAST_MODIFIED_BY VARCHAR(512),
  ADD LAST_MODIFIED_DATE TIMESTAMP; 
  
  
  
create table user_attendance(
	id  bigserial not null,
	turnstile int8 not null,
	type varchar(10) not null,
	user_profile int4 not null,
	timestamp TIMESTAMP not null,
	deleted boolean not null default false,
	
	primary key(id),
	FOREIGN KEY (user_profile) REFERENCES user_profile (id) ON DELETE CASCADE ON UPDATE CASCADE,
	FOREIGN KEY (turnstile) REFERENCES turnstile (id) ON DELETE CASCADE ON UPDATE CASCADE
); 
  
  
  
create table report_user_task(
	id bigserial not null,
	status varchar(30) not null,
	logs text,
	month int4 not null,
	year int4 not null,
	report int8,
	deleted boolean not null,
	
	primary key(id),
	FOREIGN KEY (report) REFERENCES document (ID) ON UPDATE CASCADE
);

ALTER TABLE report_user_task
  ADD CREATED_BY VARCHAR(512),
  ADD CREATED_DATE TIMESTAMP default ((now() at time zone 'utc')),
  ADD LAST_MODIFIED_BY VARCHAR(512),
  ADD LAST_MODIFIED_DATE TIMESTAMP; 
  

  
  
create table bank_hours_logs(
	id bigserial not null,
	hours_added int4,
	DAY DATE NOT NULL,
	user_profile int4 not null,
	
	primary key(id),
	FOREIGN KEY (user_profile) REFERENCES user_profile (id) ON DELETE CASCADE ON UPDATE CASCADE
);  

ALTER TABLE bank_hours_logs
  ADD CREATED_BY VARCHAR(512),
  ADD CREATED_DATE TIMESTAMP default ((now() at time zone 'utc')),
  ADD LAST_MODIFIED_BY VARCHAR(512),
  ADD LAST_MODIFIED_DATE TIMESTAMP;   
  
  
CREATE TABLE work_transfer_type(
	 id serial not null,
	 type varchar(20) not null,
	 primary key(id),
	 unique(type)
);

CREATE UNIQUE INDEX work_transfer_type_INSENSITIVE_CONSTRAINT on 
work_transfer_type (lower(type));

insert into work_transfer_type(type) values ('NATIONAL'), ('INTERNATIONAL');


CREATE TABLE work_transfer_request(
	ID bigserial NOT NULL,
	user_profile int4 NOT NULL,
	DAY DATE NOT NULL,
	type varchar(20) not null,
	
	PRIMARY KEY (ID),
	FOREIGN KEY (USER_PROFILE) REFERENCES USER_PROFILE (id) ON DELETE CASCADE ON UPDATE CASCADE,
	FOREIGN KEY (type) REFERENCES work_transfer_type (type) ON DELETE CASCADE ON UPDATE CASCADE,
	UNIQUE (USER_PROFILE, DAY)
);

ALTER TABLE work_transfer_request
  ADD CREATED_BY VARCHAR(512),
  ADD CREATED_DATE TIMESTAMP default ((now() at time zone 'utc')),
  ADD LAST_MODIFIED_BY VARCHAR(512),
  ADD LAST_MODIFIED_DATE TIMESTAMP; 
  
  
  
  

CREATE TABLE expense_report_status(
	 id serial not null,
	 status varchar(30) not null,
	 
	 primary key(id),
	 unique(status)
);

CREATE UNIQUE INDEX expense_report_status_INSENSITIVE_CONSTRAINT on 
expense_report_status (lower(status));

insert into expense_report_status(status) values ('ACCEPTED'), ('REFUSED'), ('TO_BE_PROCESSED'), ('PROCESSING'), ('PARTIALLY_ACCEPTED');

create table expense_report(
	id bigserial not null,
	made_by int4 not null,
	date_of_expence DATE not null,
	title varchar(2000) not null,
	amount float not null,
	amount_accepted float not null default 0,
	location varchar(2000),
	status VARCHAR(30) NOT NULL,
	notes text,
	processed_by int4,
	processing_by int4,
	
	PRIMARY KEY (ID),
	FOREIGN KEY (made_by) REFERENCES USER_PROFILE (id) ON DELETE CASCADE ON UPDATE CASCADE,
	FOREIGN KEY (processed_by) REFERENCES USER_PROFILE (id) ON DELETE set null ON UPDATE CASCADE,
	FOREIGN KEY (processing_by) REFERENCES USER_PROFILE (id) ON DELETE set null ON UPDATE CASCADE,
	FOREIGN KEY (status) REFERENCES expense_report_status (status) ON UPDATE CASCADE
);

ALTER TABLE expense_report
  ADD CREATED_BY VARCHAR(512),
  ADD CREATED_DATE TIMESTAMP default ((now() at time zone 'utc')),
  ADD LAST_MODIFIED_BY VARCHAR(512),
  ADD LAST_MODIFIED_DATE TIMESTAMP; 


create table expense_report_element(
	id bigserial not null,
	description text,
	amount float not null,
	attachment int8,
	expense_report int8 not null,
	accepted boolean,
	
	PRIMARY KEY (ID),
	FOREIGN KEY (attachment) REFERENCES DOCUMENT (id) on delete set null ON UPDATE CASCADE,
	FOREIGN KEY (expense_report) REFERENCES expense_report (id) ON DELETE CASCADE ON UPDATE CASCADE
);

ALTER TABLE expense_report_element
  ADD CREATED_BY VARCHAR(512),
  ADD CREATED_DATE TIMESTAMP default ((now() at time zone 'utc')),
  ADD LAST_MODIFIED_BY VARCHAR(512),
  ADD LAST_MODIFIED_DATE TIMESTAMP;


  
create table vacation_add_remove_logs(
	id bigserial not null,
	user_profile int4 not null,
	type varchar(500) not null,
	amount_of_hours float not null,
	lock_id int4,
	
	primary key(id),
	FOREIGN KEY (user_profile) REFERENCES user_profile (id) on delete cascade ON UPDATE CASCADE
);

ALTER TABLE vacation_add_remove_logs
  ADD CREATED_BY VARCHAR(512),
  ADD CREATED_DATE TIMESTAMP default ((now() at time zone 'utc')),
  ADD LAST_MODIFIED_BY VARCHAR(512),
  ADD LAST_MODIFIED_DATE TIMESTAMP;
  
  
  
  
create table database_version (
		id integer not null,
		version varchar(255) not null,

		primary key(id)
);
  
  
alter table turnstile add column AUTH_TOKEN varchar(64);
alter table completed_task add column ACTIVITY_DESCRIPTION varchar(255);
  
  




-- version 2.0.0
-- add ability to manage the budget for each task
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

