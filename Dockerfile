FROM eclipse-temurin:17-jre-alpine

WORKDIR /app

# Si usas Maven
COPY target/*.jar app.jar

# O si usas Gradle
# COPY build/libs/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]