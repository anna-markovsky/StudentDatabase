FROM maven:3.8.5-openjdk-17 AS build
COPY . .
RUN mvn clean package -DskipTests
FROM openjdk:17.0.1-jdk-slim
WORKDIR /app
COPY . .
RUN mvn clean package -DskipTests
ENV NODE_ENV production
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "demo.jar"]