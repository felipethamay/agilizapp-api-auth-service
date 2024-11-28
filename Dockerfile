FROM maven:3.8.3-openjdk-17
WORKDIR /app
COPY pom.xml .
COPY src ./src
COPY target/agilizapp-api-auth-service-1.0.jar .
RUN mvn clean install
EXPOSE 8080
CMD ["java", "-jar", "agilizapp-api-auth-service-1.0.jar"]
