# Etapa 1: compilación con Gradle
FROM eclipse-temurin:17-jdk AS builder

WORKDIR /app

COPY gradlew gradlew.bat settings.gradle build.gradle ./
COPY gradle ./gradle
COPY src ./src

RUN chmod +x ./gradlew && ./gradlew clean bootJar -x test

# Etapa 2: ejecución
FROM eclipse-temurin:17-jre

WORKDIR /app

COPY --from=builder /app/build/libs/*.jar app.jar

# Render proporciona PORT; 8085 queda como valor local por defecto.
EXPOSE 8085

ENTRYPOINT ["java", "-jar", "app.jar"]
