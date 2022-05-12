# Introduzione

Questo documento ha lo scopo di fornire le informazioni principali per configurare e eseguire il software _Time Sheet_. 

## 1. Requisiti

Per poter eseguire il software è necessario di predisporre di:

1. DBMS Postgre
2. Java Developement Kit versione 8 preinstallato sul server (Es. Oracle JDK, OpenJDK)
3. Web Server in modo da servire le risorse statiche del frontend (Es. Tomcat, Hosting, Amazon S3...)

## 2. Procedura di installazione

Per procedere all'installazione si devono eseguire i seguenti passi:

1. Inizializzazione del database
2. Setup delle password del sistema
3. Configurazione eventuale di SSL per il backend 
4. Esecuzione del backend dell'applicativo
5. Configurazione ed esecuzione del frontend dell'applicativo

### 2.1 Inizializzazione del database

Per inizializzare il database si devono eseguire gli script SQL forniti nel pacchetto di installazione. Prima di fare ciò si dovrà customizzare lo script SQL `initialize-data.sql`.


**Customizzazione dello script `initialize-data.sql`**

Lo script `initialize-data.sql` contiene molteplici customizzazioni (Nota. se si sbaglia qualcosa durante questo passaggio si può ripetere la procedura o agire direttamente sul database o dall'area di configurazione dell'applicativo per risolvere il problema). 
Molte configurazioni possono essere fatte direttamente dall'applicativo quindi in questa guida verrà mostrato come configurare soltanto quelle impostazioni che non possono essere modificate da interfaccia grafica:


**Modifica della username dell'amministatore**

L'username predefinita dell'amministratore è admin, se si vuole modificare settare il valore scelto in tutte le query dove il valore è a `admin`:

```sql
--------------------------------CHANGE USERNAME----------------------
	
INSERT INTO USERS_AUTH_DETAILS( USERNAME, HASHED_PASSWORD, LAST_PASSWORD_CHANGE_DATE, ISENABLED, 
				ISACCOUNTNONEXPIRED, ISACCOUNTNONLOCKED, ISCREDENTIALSNONEXPIRED, two_fa_enabled, permission_group_name)
 VALUES ('admin','$2a$10$0.ZOiHKGV4PPu1a6NERS3.lSw1Prko7hQEGFOrwvvfkqbYottMa/i', 
 CURRENT_TIMESTAMP at time zone 'utc', true, true, true, true, false, 'ADMINISTRATION');
 .......
 .....	
```

**Modifica del nome dell'azienda**

```sql
INSERT INTO COMPANY(NAME, DESCRIPTION) VALUES ('COMPANY NAME', 'COMPANY DESCRIPTION');
```

**Modifica dei dati dell'amministratore:**

Nelle seguenti istruzioni sql si dovranno modificare l'email dell'amministatore e il numero di telefono e al posto della username _admin_ dovrà essere usato quello scelto in precedenza:

```sql
INSERT INTO user_profile(id, name, surname, sex, email, phone_number, birth_date)
select uad.id as FK_ID_USERS_AUTH_DETAILS , 'Admin', 'Admin', 'M', 'admin@gmail.it', '+39 333333333', '2000-09-07'
from USERS_AUTH_DETAILS uad  where uad.username='admin';
	
	
INSERT INTO user_contacts(
	fk_id_users_auth_details, user_contact_type, c_value, verified)
	select uad.id as fk_id_users_auth_details, 'PHONE_NUMBER', '+39 333333333', true 
	from USERS_AUTH_DETAILS uad  where uad.username='admin';
	
INSERT INTO user_contacts(
	fk_id_users_auth_details, user_contact_type, c_value, verified)
	select uad.id as fk_id_users_auth_details, 'EMAIL_ADDRESS', 'admin@gmail.it', true  
	from USERS_AUTH_DETAILS uad  where uad.username='admin';
```


Una volta customizzato lo script passiamo all'esecuzione: 

1. Apriamo un terminale sql usando PgAdmin ed eseguiamo lo script `deploy-tables.sql`. Questo script andrà a creare lo schema e le varie tabelle usate dal programma
2. Sempre su PgAdmin eseguiamo lo script `initialize-data.sql` che hai customizzato

### 2.2 Setup delle password del sistema

Per poter eseguire il programma per la prima volta si dovranno scegliere due password:

1. Password per firmare i token
2. Password per cifrare i file (Questa password dovrà essere salvata in un luogo sicuro, se questa password verrà persà i dati non potranno essere recuperati)

Queste due password devono essere sicure e avere una lunghezza di circa 54 caratteri alfanumerici.

### 2.3 Esecuzione del backend dell'applicativo

Per poter eseguire correttamente l'applicativo si dovranno passare come vari argomenti al programma. I seguenti argomenti andranno passati con il formato `--${PROPERTY_KEY}=${VALORE}` (Esempio `--spring.main.banner-mode=off`):

**Argomenti per il collegamento al**
1. `persistence.datasource.url` che indica l'url del datasource (Es. jdbc:postgresql://127.0.0.1:5432/postgres)
2. `persistence.datasource.username` password del datasource (Es. postgres)
3. `persistence.datasource.password` password del datasource (Es. password)

**Argomenti per il gestore dei file cifrati**

1. `fileManager.EncryptedFSFileManagerPassword.password` password con la quale verranno cifrati i file (ad esempio jdhsy32y876skjy3i3thg...), la lunghezza dovrà essere almeno di 54 caratteri alfanumerici. Nota: QUESTA PASSWORD DOVRÀ ESSERE SALVATA IN MODO SICURO E NON POTRA ESSERE MODIFICATA
2. `fileManager.EncryptedFSFileManagerPassword.basePath` indica il percorso dove i file cifrati verranno salvati (Es. /opt/attendance-management/files)

**Argomenti per il gestore dei file NON cifrati**

1. `fileManager.EncryptedFSFileManagerPassword.basePath` indica il percorso dove i file verranno salvati (Es. /opt/attendance-management/files)

**Argomenti per il gestore dei file temporanei**

1. `temp-file-directory` indica il percorso dove i file verranno salvati (Es. /opt/attendance-management/TEMP-files)

**Argomenti per firmare i token JWT**

1. `security.jwt.secret` password con la quale verranno firmati i token jwt i file (ad esempio jdhsy32y83dz2defe763thgds87s...), la lunghezza dovrà essere almeno di 54 caratteri alfanumerici.


**Esampio di comando di esecuzione**

```sh
java -jar ${PATH_TO_JAR_FILE} --security.jwt.secret=x8NbAp6tSEfsUHXUF46AElX6UyRTYhq33fOABk1oiAwGG24LoF4vHENCAcI70YdQwmLWV67TFQaSWjqo  --temp-file-directory=C:/CustomTempData/AttendanceManagement_Files/TEMP --fileManager.EncryptedFSFileManagerPassword.basePath=C:/CustomTempData/AttendanceManagement_Files --fileManager.EncryptedFSFileManagerPassword.basePath=C:/CustomTempData/AttendanceManagement_Files --fileManager.EncryptedFSFileManagerPassword.password=n4VyVZYhga2rhUWrh3PJoJNqjo07hAkrA7tvJuwFaPTV7lCiVybCvfaXu9e7nLkWN0gjF0NlIZzJAG9L --persistence.datasource.url=jdbc:postgresql://127.0.0.1:5432/postgres --persistence.datasource.username=postgres --persistence.datasource.password=root
```
WARNING: Si consiglia di non salvare il comando di avvio del server in modo che se il server viene compromesso sarà più difficile recuperare le password usate nell'applicativo.

### 2.4 Configurazione eventuale di SSL per il backend

Per configurare il backend in modo da usare una connessione SSL si dovrà possedere un certificato e permettere all'applicazione di accedervi.  
Si dovranno passare i seguenti argomenti al momento dell'esecuzione del programma: 

1. `server.ssl.key-store` path al keystore (Es. C:\CustomTempData\AttendanceManagement_Files\localhost.p12)

2. `server.ssl.key-store-password`  password del keystore (Es. password)

3. `server.ssl.key-alias` alias (Es. localhost)

4. `server.ssl.key-store-type` tipo di keystore (Es. pkcs12)

5. `server.ssl.key-password` password della chiave (Es. password)

6. `server.port` indica la porta su cui esporre l'applicativo (Es. 8443)

Se non si possiede di un certificarlo, si può creare gratuitamente con Let's Encrypt.  

Questi argomenti andranno passati al programma ad esempio:
> java -jar --server.ssl.key-store=C:\CustomTempData\AttendanceManagement_Files\localhost.p12 --server.ssl.key-store-password=password --server.ssl.key-store-type=pkcs12 --server.ssl.key-alias=localhost --server.ssl.key-password=password --server.port=8443  

Una volta terminata la configurazione per essere sicuri che tutto funziona collegarsi all'url `https://${hostname}:8443/version`.
WARNING: Si consiglia di non salvare il comando di avvio del server in modo che se il server viene compromesso sarà più difficile recuperare le password del keystore.


### 2.5 Esecuzione del programma

A questo punto il programma può essere esguito usando il comando java -jar con gli argomenti.  
Se si è su linux per salvare il file di log si consiglia di usare il comando:

> nohup java -jar my.jar > /${PATH_LOG_FILE} &

Questo file potrà poi essere letto da logstash (Usando il formato **logback**) per poter visionare i log su kibana.




