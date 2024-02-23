# Stage 1: Build stage
FROM maven:3.8.5-openjdk-17 AS build
WORKDIR /app
COPY . .
RUN mvn clean package -DskipTests

# Stage 2: Final image
FROM openjdk:17.0.1-jdk-slim
WORKDIR /app
COPY --from=build /app/target/demo-0.0.1-SNAPSHOT.jar demo.jar
ENV NODE_ENV production
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "demo.jar"]