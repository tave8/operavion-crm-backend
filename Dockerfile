FROM eclipse-temurin:21-jdk-alpine
COPY target/app.jar app.jar 
ENTRYPOINT ["java", "-jar", "app.jar"]
