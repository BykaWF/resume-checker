FROM eclipse-temurin:23-jdk AS build

# Set the working directory inside the container
WORKDIR /app

# Copy the Maven wrapper and necessary files to the container
COPY mvnw .
COPY .mvn .mvn
COPY pom.xml .

# Copy the source code into the container
COPY src src

# Set build-time environment variables
ARG SPRING_AI_API_KEY
ARG SPRING_AI_COMPLETION_URL
ARG SPRING_AI_BASE_URL
ARG RAILWAY_DATABASE_URL
ARG RAILWAY_DATABASE_USERNAME
ARG RAILWAY_DATABASE_PASSWORD
ARG ALLOWED_ORIGINS
ARG AUTH_ISSUER_URI
ARG AUTH_JWK_SET_URI

# Install Maven and build the application
RUN ./mvnw clean package -DskipTests

# Now that the app is built, copy the JAR into the final image
FROM eclipse-temurin:23-jdk AS runtime

# Set working directory for runtime environment
WORKDIR /app

# Copy the JAR file built in the previous stage into the runtime container
COPY --from=build /app/target/*.jar app.jar

# Set runtime environment variables
ENV SPRING_AI_API_KEY=${SPRING_AI_API_KEY}
ENV SPRING_AI_COMPLETION_URL=${SPRING_AI_COMPLETION_URL}
ENV SPRING_AI_BASE_URL=${SPRING_AI_BASE_URL}
ENV RAILWAY_DATABASE_URL=${RAILWAY_DATABASE_URL}
ENV RAILWAY_DATABASE_USERNAME=${RAILWAY_DATABASE_USERNAME}
ENV RAILWAY_DATABASE_PASSWORD=${RAILWAY_DATABASE_PASSWORD}
ENV ALLOWED_ORIGINS=${ALLOWED_ORIGINS}
ENV AUTH_ISSUER_URI=${AUTH_ISSUER_URI}
ENV AUTH_JWK_SET_URI=${AUTH_JWK_SET_URI}

# Expose the application port
EXPOSE 8080

# Command to run the app when the container starts
ENTRYPOINT ["java", "-jar", "app.jar"]
