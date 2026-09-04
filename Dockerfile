# Download and cache the Maven dependencies.
FROM eclipse-temurin:17-jdk-alpine AS dependencies

WORKDIR /app

COPY .mvn/ .mvn/
COPY mvnw pom.xml ./

RUN chmod +x mvnw \
    && ./mvnw -B dependency:go-offline


# Package the application as an executable JAR.
FROM dependencies AS builder

WORKDIR /app

COPY src ./src

RUN ./mvnw -B package -DskipTests


# Create the production image with runtime components only.
FROM eclipse-temurin:17-jre-alpine AS runtime

WORKDIR /app

ENV SERVER_PORT=8080

RUN addgroup -S spring \
    && adduser -S spring -G spring

COPY --from=builder --chown=spring:spring /app/target/*.jar app.jar

USER spring

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]