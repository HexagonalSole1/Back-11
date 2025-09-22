# ===== BUILD STAGE =====
FROM eclipse-temurin:17-jdk-alpine AS build

WORKDIR /app

# Copiar archivos de configuración de Maven
COPY pom.xml .
COPY .mvn .mvn
COPY mvnw .

# Dar permisos de ejecución
RUN chmod +x mvnw

# Descargar dependencias (se cachea esta capa)
RUN ./mvnw dependency:go-offline

# Copiar código fuente
COPY src ./src

# Compilar el proyecto
RUN ./mvnw clean package -DskipTests

# ===== RUN STAGE =====
FROM eclipse-temurin:17-jre-alpine

WORKDIR /app

# Copiar el JAR compilado desde la etapa anterior
COPY --from=build /app/target/*.jar app.jar

# Puerto que usará Railway
EXPOSE 8080

# Comando de inicio
ENTRYPOINT ["java", "-jar", "app.jar"]