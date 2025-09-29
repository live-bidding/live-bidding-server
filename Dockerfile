# Dockerfile

FROM amazoncorretto:21-alpine-jdk AS builder
WORKDIR /app
COPY . .
RUN ./gradlew build

FROM amazoncorretto:21-alpine-jre
WORKDIR /app

COPY --from=builder /app/build/libs/*.jar ./app.jar

ENTRYPOINT ["java", "-jar", "app.jar"]
