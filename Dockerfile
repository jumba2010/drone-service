# Use a JDK 17 base image
FROM azul/zulu-openjdk:17

# Set the working directory to /app
WORKDIR /app

# Copy the Gradle files to the container
COPY gradlew .
COPY gradle gradle

# Run the Gradle Wrapper to download the dependencies
RUN ./gradlew --no-daemon --console plain --quiet --refresh-dependencies

# Copy the rest of the application files to the container
COPY . .

# Build the application
RUN ./gradlew build

# Start the application
CMD ["java", "-jar", "build/libs/drone-service-0.0.1-SNAPSHOT.jar"]
