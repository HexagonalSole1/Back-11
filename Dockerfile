# Para el backend (archivo Dockerfile en la carpeta del backend)
FROM eclipse-temurin:17-jre-alpine

WORKDIR /app
COPY backend/target/*.jar app.jar

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]