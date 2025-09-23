FROM gradle:8.5-jdk21 AS build-env
WORKDIR /app
COPY . .
RUN gradle build -x test

FROM amazoncorretto:21
WORKDIR /app
COPY --from=build-env /app/build/libs/*.jar app.jar
EXPOSE 8081
ENTRYPOINT ["java", "-jar", "app.jar"] 