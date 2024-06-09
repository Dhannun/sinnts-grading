FROM openjdk:17-alpine
WORKDIR /app
COPY /target/wqp-api.jar /app/app.jar

EXPOSE 7890
ENTRYPOINT ["java", "-jar", "app.jar"]