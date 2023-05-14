# Stage 1: Build the application
FROM adoptopenjdk/openjdk8:ubi AS build
WORKDIR /app
COPY pom.xml .
RUN yum install -y maven
RUN mvn dependency:go-offline -B
COPY src ./src
RUN mvn package

# Stage 2: Run the application
FROM openjdk:8-jdk-alpine
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","app.jar"]