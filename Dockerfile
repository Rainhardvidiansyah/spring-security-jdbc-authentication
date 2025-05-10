
# Use an official OpenJDK runtime as a parent image
FROM eclipse-temurin:17-jdk

# Set the working directory to /app
WORKDIR /app

# Copy the executable jar file and the application.properties file to the container
COPY target/*.jar erp-module-rainhard.jar

# Run the app
ENTRYPOINT ["java", "-jar", "erp-module-rainhard.jar"]

#
## Set the command to run the Spring Boot application
#CMD ["java", "-jar", "rainhard-modules-erp-0.0.1.SNAPSHOT.jar"]