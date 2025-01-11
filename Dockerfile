FROM eclipse-temurin:21-jdk-alpine

WORKDIR /app
COPY target/ticket-consumer.jar /app/ticket-consumer.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app/ticket-consumer.jar"]