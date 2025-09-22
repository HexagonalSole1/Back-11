# Etapa de construcción con Gradle
FROM eclipse-temurin:17-jdk-alpine AS builder

# Establecer directorio de trabajo
WORKDIR /app

# Copiar archivos de configuración de Gradle
COPY gradle gradle
COPY gradlew .
COPY gradlew.bat .
COPY gradle.properties .
COPY settings.gradle.kts .
COPY build.gradle.kts .

# Dar permisos de ejecución a gradlew
RUN chmod +x ./gradlew

# Descargar dependencias (esto se cachea si no cambia build.gradle.kts)
RUN ./gradlew dependencies --no-daemon

# Copiar código fuente del módulo app
COPY app ./app

# Compilar la aplicación
RUN ./gradlew :app:build --no-daemon -x test

# Etapa de producción
FROM eclipse-temurin:17-jre-alpine

# Crear usuario no-root para seguridad
RUN addgroup -g 1001 -S appgroup && \
    adduser -u 1001 -S appuser -G appgroup

# Establecer directorio de trabajo
WORKDIR /app

# Copiar el JAR/APK compilado desde la etapa de builder
# Nota: Si es una app Android, generará un APK, no un JAR ejecutable
COPY --from=builder /app/app/build/libs/*.jar app.jar 2>/dev/null || \
     COPY --from=builder /app/app/build/outputs/apk/release/*.apk app.apk

# Cambiar propietario de los archivos al usuario no-root
RUN chown -R appuser:appgroup /app

# Cambiar al usuario no-root
USER appuser

# Exponer el puerto 8080
EXPOSE 8080

# Configurar variables de entorno
ENV JAVA_OPTS="-Xmx512m -Xms256m"

# Comando para ejecutar la aplicación
# Nota: Esto solo funciona si es una aplicación backend, no una app Android
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]