FROM openjdk:8-jre-alpine

WORKDIR /app

ARG JAR_FILE=build/libs/*.jar
COPY ${JAR_FILE} amate-be.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "amate-be.jar"]
