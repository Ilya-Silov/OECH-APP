FROM maven:3.9.6-eclipse-temurin-21 as builder
WORKDIR /code
COPY . .
RUN mvn -f pom.xml clean package -DskipTests


FROM openjdk:21-jdk-slim
ARG JAR_FILE=/code/target/*.jar
COPY --from=builder ${JAR_FILE} app.jar
ENTRYPOINT ["java","-jar","/app.jar"]