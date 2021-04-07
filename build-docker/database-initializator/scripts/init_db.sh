#!/bin/sh

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