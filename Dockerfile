# Stage 1: Build the application using Maven
FROM eclipse-temurin:23-jdk AS build

WORKDIR /app


COPY mvnw .
COPY .mvn .mvn


COPY pom.xml .
RUN ./mvnw dependency:resolve

COPY src ./src

RUN ./mvnw clean package -DskipTests


# Stage 2: Create the final runtime image
FROM eclipse-temurin:23-jre

WORKDIR /app

COPY --from=build /app/target/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
