FROM openjdk:8-jdk-alpine
RUN addgroup -S spring && adduser -S spring -G spring

COPY ./scripts/wait-and-init.sh /opt/wait-and-init.sh

RUN chmod 755 /opt/wait-and-init.sh

RUN chown spring:spring /opt

USER spring:spring

ARG JAR_FILE=*.jar

COPY ${JAR_FILE} app.jar


ENTRYPOINT ["sh","/opt/wait-and-init.sh"]