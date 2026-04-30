# Etapa 1: build com Maven + JDK
FROM maven:3.9.9-eclipse-temurin-17 AS build
WORKDIR /app
COPY . .
RUN mvn clean package -DskipTests

# Etapa 2: runtime leve
FROM eclipse-temurin:17-jdk
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar

# Render usa a porta do $PORT
EXPOSE 8080
ENTRYPOINT ["java","-jar","app.jar"]