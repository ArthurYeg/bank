
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app
COPY target/bank-0.0.1-SNAPSHOT.jar /app/bank.jar
ENTRYPOINT ["java", "-jar", "bank.jar"]
EXPOSE 8089
EXPOSE 8189
