# Maven Build Stage
FROM maven:3.8.5-openjdk-17 AS build
WORKDIR /app
COPY . .
RUN mvn clean package -Pprod -DskipTests

# Final Docker Build Stage
FROM eclipse-temurin:17-alpine
WORKDIR /app
COPY --from=build /app/target/QuickMaster-1.0.0.jar demo.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","demo.jar"]