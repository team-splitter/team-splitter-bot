#FROM openjdk:11
FROM adoptopenjdk/openjdk11:alpine-jre
ARG JAR_FILE=team-splitter-app/target/*.jar
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["java","-jar","/app.jar"]