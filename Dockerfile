FROM eclipse-temurin:21-jdk-alpine as builder
WORKDIR /app
COPY ../gradlew .
COPY ../gradle gradle
COPY ../build.gradle .

RUN ./gradlew dependencies
COPY account-access-consent-service/src src
COPY account-access-consent-client-api/build/generated/openapi/src src
RUN ./gradlew clean build -x test

FROM eclipse-temurin:21-jre-alpine
VOLUME /tmp
COPY --from=builder /app/build/libs/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","/app.jar"]