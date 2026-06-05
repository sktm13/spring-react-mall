# =========================
# Build Stage
# =========================
FROM eclipse-temurin:21-jdk AS build

WORKDIR /app

COPY gradlew .
COPY gradle ./gradle
COPY build.gradle .
COPY settings.gradle .

RUN chmod +x ./gradlew

COPY src ./src

RUN ./gradlew clean bootJar -x test


# =========================
# Run Stage
# =========================
FROM eclipse-temurin:21-jre

WORKDIR /app

COPY --from=build /app/build/libs/*.jar app.jar

# local 파일 저장소
RUN mkdir -p /app/upload

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "/app/app.jar"]