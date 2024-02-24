
FROM maven:3.8.5-openjdk-17 AS build
COPY . .
RUN mvn clean package -Pprod -DskipTests
FROM eclipse-temurin:17-alpine
COPY --from=build /target/QuickMaster-1.0.0.jar demo.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","demo.jar"]