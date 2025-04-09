FROM eclipse-temurin:23-jdk

WORKDIR /app

COPY mvnw .
COPY .mvn .mvn
COPY pom.xml .


COPY src src

# Package the application (skip tests)
RUN ./mvnw package -DskipTests

# If your app is packaged as a JAR
# The path may need to be adjusted based on your build output
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} app.jar

# Configure container to run the compiled app
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
