FROM alpine

RUN apk upgrade --no-cache && \
    apk add --no-cache postgresql-client

COPY './sql/*.sql' '/tmp/'

COPY ./scripts/init_db.sh init_db.sh

RUN chmod 755 init_db.sh

ENTRYPOINT ["./init_db.sh"]