FROM openjdk:25

WORKDIR /app

ARG JAR_FILE=build/libs/*.jar
COPY ${JAR_FILE} amate-be.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "amate-be.jar"]
