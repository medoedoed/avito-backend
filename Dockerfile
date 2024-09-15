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
ENV POSTGRES_CONN=
ENV POSTGRES_JDBC_URL=
ENV POSTGRES_USERNAME=
ENV POSTGRES_PASSWORD=
ENV POSTGRES_HOST=
ENV POSTGRES_PORT=
ENV POSTGRES_DATABASE=

ENTRYPOINT ["java", "-XX:+UnlockExperimentalVMOptions", "-XX:+UseContainerSupport", "-Djava.security.egd=file:/dev/./urandom", "-Dspring.datasource.url=${POSTGRES_JDBC_URL}", "-Dspring.datasource.username=${POSTGRES_USERNAME}", "-Dspring.datasource.password=${POSTGRES_PASSWORD}", "-jar", "/app/spring-boot-application.jar"]
