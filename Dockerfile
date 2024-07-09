FROM openjdk:latest
WORKDIR /app
COPY build/libs/your-spring-app.jar /app
CMD ["java", "-jar", "your-spring-app.jar"]