FROM openjdk:17-alpine
WORKDIR /app
COPY /target/sinnts.jar /app/app.jar

EXPOSE 7890
ENTRYPOINT ["java", "-jar", "app.jar"]