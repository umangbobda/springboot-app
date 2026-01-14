FROM eclipse-temurin:17-jre

WORKDIR /app

COPY target/demoProject-0.0.1-SNAPSHOT.jar app.jar
COPY Player.csv Player.csv

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
