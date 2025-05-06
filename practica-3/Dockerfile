FROM gradle:8.7-jdk17 AS build

WORKDIR /app

COPY build.gradle settings.gradle gradle.properties* ./
COPY gradle ./gradle
COPY src ./src

RUN gradle shadowJar

FROM openjdk:17-jdk-slim

WORKDIR /app

COPY --from=build /app/build/libs/app.jar app.jar

EXPOSE 7000

ENTRYPOINT ["java", "-jar", "app.jar"]
