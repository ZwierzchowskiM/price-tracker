# Importing OpenJDK 21 and copying required files for build
FROM openjdk:21-jdk AS build
WORKDIR /app
COPY pom.xml .
COPY src src

# Copy Maven wrapper
COPY mvnw .
COPY .mvn .mvn

# Set execution permission for the Maven wrapper
RUN chmod +x ./mvnw

# Build the application and skip tests
RUN ./mvnw clean package -DskipTests

# Stage 2: Create the final Docker image using OpenJDK 21
FROM openjdk:21-jdk
VOLUME /tmp

# Copy the JAR from the build stage
COPY --from=build /app/target/*.jar app.jar

# Set the entrypoint to run the application
ENTRYPOINT ["java","-jar","/app.jar"]

# Expose port 8080 (or any dynamic port if necessary)
EXPOSE 8080