# ---- Build stage ----
FROM maven:3.9.9-eclipse-temurin-21 AS build
WORKDIR /app
COPY pom.xml .
RUN mvn dependency:go-offline
COPY src ./src
RUN mvn clean package -DskipTests

# ---- Run stage ----
FROM eclipse-temurin:21-jre
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar

# Render uses PORT env var (usually 10000)
EXPOSE 10000

ENTRYPOINT ["java", "-Djava.io.tmpdir=/tmp", "-Dserver.port=${PORT}", "-jar", "app.jar"]
