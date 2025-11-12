# ===========================
# 1️⃣ Build Stage
# ===========================
FROM eclipse-temurin:21-jdk-alpine AS build

WORKDIR /app

COPY pom.xml .
COPY mvnw .
COPY .mvn .mvn
RUN chmod +x mvnw

COPY domain/ domain/
COPY application/ application/
COPY api/ api/
COPY infrastructure/ infrastructure/

RUN ./mvnw dependency:go-offline -B

RUN ./mvnw clean package -pl api -am -DskipTests

RUN ./mvnw clean package -pl infrastructure -am -DskipTests

# ===========================
# Execution Stage
# ===========================
FROM eclipse-temurin:21-jre-alpine

WORKDIR /App

COPY --from=build /app/infrastructure/target/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]