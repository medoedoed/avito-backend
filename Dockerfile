FROM gradle:8.10-alpine AS build
COPY --chown=gradle:gradle . /home/gradle/src
WORKDIR /home/gradle/src
RUN gradle build --no-daemon

FROM openjdk:21

EXPOSE 8080

RUN mkdir /app

COPY --from=build /home/gradle/src/build/libs/*.jar /app/spring-boot-application.jar

ENV SERVER_ADDRESS=0.0.0.0
ENV SERVER_PORT=8080
ENV POSTGRES_CONN=jdbc:postgresql://localhost:5432/db
ENV POSTGRES_JDBC_URL=jdbc:postgresql://postgres:5432/db
ENV POSTGRES_USERNAME=username
ENV POSTGRES_PASSWORD=password
ENV POSTGRES_HOST=localhost
ENV POSTGRES_PORT=5432
ENV POSTGRES_DATABASE=db

ENTRYPOINT ["java", "-XX:+UnlockExperimentalVMOptions", "-XX:+UseContainerSupport", "-Djava.security.egd=file:/dev/./urandom", "-Dspring.datasource.url=${POSTGRES_JDBC_URL}", "-Dspring.datasource.username=${POSTGRES_USERNAME}", "-Dspring.datasource.password=${POSTGRES_PASSWORD}", "-jar", "/app/spring-boot-application.jar"]