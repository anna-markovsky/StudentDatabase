# Stage 1: Build stage
FROM maven:3.8.5-openjdk-17 AS build
WORKDIR /usr/src/app
COPY . .
RUN mvn clean package -DskipTests

# Stage 2: Final image
FROM openjdk:17.0.1-jdk-slim
WORKDIR /app
COPY --from=build /usr/src/app/demo/target/demo-0.0.1-SNAPSHOT.jar demo.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "demo.jar"]