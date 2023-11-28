# Start with a base image containing Java runtime (version 11)
FROM maven:3.6.3-jdk-11-slim AS build

# Add the application's jar to the container
COPY src /home/app/src
COPY pom.xml /home/app

# Package the application
RUN mvn -f /home/app/pom.xml clean package

# Use OpenJDK for running the application
FROM openjdk:11-jre-slim
COPY --from=build /home/app/target/*.jar /usr/local/lib/app.jar

# Expose the port the app runs on
EXPOSE 8080

# Run the jar file
ENTRYPOINT ["java","-jar","/usr/local/lib/app.jar"]
