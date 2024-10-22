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

# Set the working directory
WORKDIR /app

# Install dependencies for Google Chrome
RUN apk add --no-cache wget bash curl

# Install Google Chrome from the community repository
RUN apk add --no-cache chromium

# Set CHROME_BIN and CHROME_PATH environment variables
ENV CHROME_BIN=/usr/bin/chromium-browser
ENV CHROME_PATH=/usr/lib/chromium/

# Copy the JAR from the build stage
COPY --from=build /app/target/*.jar app.jar

# Set the entrypoint to run the application
ENTRYPOINT ["java","-jar","/app.jar"]

# Expose port 8080 (or any dynamic port if necessary)
EXPOSE 8080