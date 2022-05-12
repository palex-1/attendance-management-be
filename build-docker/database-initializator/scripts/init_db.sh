#!/bin/sh

CURRENT_VERSION="2.0.0"

DATABASE_SCHEMA='attendance_management'

# Variables $DATABASE_USER, $DATABASE_PASSWORD, $DATABASE_HOST, $DATABASE_PORT

if [ -z "$DATABASE_USER" ] || [ -z "$DATABASE_PASSWORD" ] || [ -z "$DATABASE_HOST" ] || [ -z "$DATABASE_PORT" ]; then
    echo "ERROR: You need to provide all variables to connect to initialize the database"
    exit 1
else
    echo "All required parameters are ok..."
fi



database_exists_check=$(psql -t -c "SELECT schema_name FROM information_schema.schemata WHERE schema_name = '$DATABASE_SCHEMA'" postgresql://$DATABASE_USER:$DATABASE_PASSWORD@$DATABASE_HOST:$DATABASE_PORT/)

echo ${database_exists_check}


# check if variable is null
if [ -z "$database_exists_check" ] || [ $database_exists_check != $DATABASE_SCHEMA ]; then
    echo "Table creation start..."
    psql postgresql://$DATABASE_USER:$DATABASE_PASSWORD@$DATABASE_HOST:$DATABASE_PORT/ -f "/tmp/schema.sql"
    echo "Table creation end..."

    echo "Data initializing start..."
    psql postgresql://$DATABASE_USER:$DATABASE_PASSWORD@$DATABASE_HOST:$DATABASE_PORT/ -f "/tmp/data.sql"
    echo "Data initializing end..."

else
    echo "Table already created..."    
fi



database_version=$(psql -t -c "SELECT version FROM attendance_management.database_version WHERE id = 1" postgresql://$DATABASE_USER:$DATABASE_PASSWORD@$DATABASE_HOST:$DATABASE_PORT/)
    
old_major=${database_version%%.*}          # Delete first dot and what follows.
old_fix=${database_version##*.}           # Delete up to last dot.
old_minor=${database_version##$old_major.}       # Delete first number and dot. 
old_minor=${old_minor%%.$old_fix}      # Delete dot and last number.


#echo $old_major $old_minor $old_fix

new_major=${CURRENT_VERSION%%.*}          # Delete first dot and what follows.
new_fix=${CURRENT_VERSION##*.}           # Delete up to last dot.
new_minor=${CURRENT_VERSION##$new_major.}       # Delete first number and dot. 
new_minor=${new_minor%%.$new_fix}

echo $new_major $new_minor $new_fix

# -a means and condition

if [ "$new_major" -lt "$old_major" ] || [ "$new_major" -eq "$old_major" -a "$new_minor" -lt "$old_minor" ]  || 
         [ "$new_major" -eq "$old_major" -a "$new_minor" -eq "$old_minor" -a "$new_fix" -lt "$old_fix" ]; then

    echo "************FATAL ERROR**************"
    echo "ERROR"
    echo "ERROR: Cannot downgrade database!!!"
    echo "*********************************"

    exit 1
fi


# version 1.1.0 updates
if [ "$old_major" -eq 1 -a "$old_minor" -eq 0 -a "$old_fix" -eq 0 ]; then
    echo "applying Update version 1.1.0 start..."
    psql postgresql://$DATABASE_USER:$DATABASE_PASSWORD@$DATABASE_HOST:$DATABASE_PORT/ -f "/tmp/update_1.1.0.sql"
    old_major=1
    old_minor=1
    old_fix=0
    echo "applying Update version 1.1.0 end..."
fi 


# version 2.0.0 updates
if [ "$old_major" -eq 1 -a "$old_minor" -eq 1 -a "$old_fix" -eq 0 ]; then
    echo "applying Update version 2.0.0 start..."
    psql postgresql://$DATABASE_USER:$DATABASE_PASSWORD@$DATABASE_HOST:$DATABASE_PORT/ -f "/tmp/update_2.0.0.sql"
    old_major=2
    old_minor=0
    old_fix=0
    echo "applying Update version 2.0.0 end..."
fi 

echo "Database successfully patched.."  