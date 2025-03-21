# Use Java 23 image as the base image
FROM eclipse-temurin:23-jdk

# Define build arguments
ARG SPRING_AI_API_KEY
ARG SPRING_AI_COMPLETION_URL
ARG SPRING_AI_BASE_URL
ARG RAILWAY_DATABASE_URL
ARG RAILWAY_DATABASE_USERNAME
ARG RAILWAY_DATABASE_PASSWORD

# Set environment variables
ENV SPRING_AI_API_KEY=${SPRING_AI_API_KEY}
ENV SPRING_AI_COMPLETION_URL=${SPRING_AI_COMPLETION_URL}
ENV SPRING_AI_BASE_URL=${SPRING_AI_BASE_URL}
ENV RAILWAY_DATABASE_URL=${RAILWAY_DATABASE_URL}
ENV RAILWAY_DATABASE_USERNAME=${RAILWAY_DATABASE_USERNAME}
ENV RAILWAY_DATABASE_PASSWORD=${RAILWAY_DATABASE_PASSWORD}

# Set working directory in the container
WORKDIR /app

# Copy Maven wrapper files first (if using Maven)
COPY mvnw .
COPY .mvn .mvn
COPY pom.xml .

# Copy source code
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