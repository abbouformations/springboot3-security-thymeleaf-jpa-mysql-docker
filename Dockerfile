#Maven Build
#FROM maven:3.8.3-openjdk-11-slim AS builder
#COPY pom.xml /app/
#COPY src /app/src
#ENTRYPOINT ["mvn", "clean", "install"]

#Run
FROM openjdk:17-oracle
VOLUME /tmp
COPY target/*.jar app.jar
ENTRYPOINT ["java","-jar","/app.jar"]