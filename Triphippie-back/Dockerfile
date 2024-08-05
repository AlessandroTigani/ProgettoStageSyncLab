FROM openjdk:21
WORKDIR /app
COPY target/*.jar /app/triphippie.jar
COPY target/.env /app/.env
CMD ["java", "-jar", "/app/triphippie.jar"]