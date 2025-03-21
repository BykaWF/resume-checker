FROM eclipse-temurin:23-jdk

WORKDIR /app

COPY mvnw .
COPY .mvn .mvn
COPY pom.xml .

COPY .env .

COPY src src

RUN ./mvnw package -DskipTests


ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} app.jar

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]