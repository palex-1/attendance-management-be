#!/bin/sh

echo 'Starting Backend...'
# wait the inizialization of the database
sleep 40

java -jar /app.jar -Dlog4j2.formatMsgNoLookups=true