FROM openjdk:8-jdk-alpine

LABEL author="Abrar"

RUN apk update && apk add bash

WORKDIR /usr/src/app

COPY target/fairbilling-0.0.1-SNAPSHOT.jar fairBilling.jar

# Copy input directory to /logs/ folder
COPY src/main/resources/input/*.txt /logs/

ENTRYPOINT ["java", "-jar", "/usr/src/app/fairBilling.jar"]