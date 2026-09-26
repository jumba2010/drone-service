# syntax=docker/dockerfile:1.7

# ---- Build stage: compile and test with the Gradle wrapper on JDK 17 ----
FROM eclipse-temurin:17-jdk-jammy AS build
WORKDIR /workspace

# Resolve dependencies first so they are cached between source changes
COPY gradlew settings.gradle build.gradle ./
COPY gradle gradle
RUN --mount=type=cache,target=/root/.gradle ./gradlew --no-daemon dependencies > /dev/null

COPY src src
RUN --mount=type=cache,target=/root/.gradle ./gradlew --no-daemon bootJar

# ---- Runtime stage: slim JRE, non-root user ----
FROM eclipse-temurin:17-jre-jammy
RUN useradd --uid 10001 --create-home --shell /usr/sbin/nologin appuser
WORKDIR /app
COPY --from=build /workspace/build/libs/drone-service-*-SNAPSHOT.jar app.jar
USER appuser

EXPOSE 8085
ENTRYPOINT ["java", "-XX:MaxRAMPercentage=75.0", "-jar", "app.jar"]
