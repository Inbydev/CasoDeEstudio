# ------------------------------------------------------------------------------
# STAGE 1: Build stage
# ------------------------------------------------------------------------------
FROM eclipse-temurin:21-jdk-alpine-3.24 AS builder
WORKDIR /app

# Copiamos el wrapper de Gradle y sus configuraciones
COPY gradlew .
COPY gradle ./gradle
COPY build.gradle.kts settings.gradle.kts ./

# Copiamos el código fuente
COPY src/main/kotlin/dev/ia/caso/Funcional.kt ./src/main/kotlin/dev/ia/caso/

# Damos permisos de ejecución al wrapper de Gradle
RUN chmod +x gradlew

# Construimos la distribución de la aplicación
RUN ./gradlew installDist --no-daemon

# ------------------------------------------------------------------------------
# STAGE 2: Runtime stage
# ------------------------------------------------------------------------------
FROM eclipse-temurin:21-jre-alpine-3.24
WORKDIR /app

# Copiamos la aplicación ya compilada desde la etapa de construcción
COPY --from=builder /app/build/install/CasoDeEstudio ./

# Comando para ejecutar la aplicación
CMD ["./bin/CasoDeEstudio"]
