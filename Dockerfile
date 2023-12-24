FROM amazoncorretto:17-alpine-jdk

WORKDIR /app

COPY ./app.jar ./

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]

