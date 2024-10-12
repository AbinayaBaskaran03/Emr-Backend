FROM maven:3.8.4-openjdk-17 AS build
COPY . .
RUN mvn clean package -DskipTests

FROM openjdk:17-jdk-slim
COPY --from=build /target/app-1.0-emr.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
