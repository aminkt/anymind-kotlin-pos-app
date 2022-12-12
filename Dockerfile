FROM gradle:7.5.1 AS build
COPY --chown=gradle:gradle . /home/gradle/src
WORKDIR /home/gradle/src
RUN gradle build

FROM openjdk:11-jre-slim
ARG APP_VERSION='0.0.1-SNAPSHOT'
EXPOSE 8080
COPY --from=build /home/gradle/src/build/libs/pos-0.0.1-SNAPSHOT.jar /app/pos.jar
RUN bash -c 'touch /app/pos.jar'
ENTRYPOINT ["java", "-jar", "/app/pos.jar", "--spring.datasource.url=jdbc:mysql://${MYSQL_HOST}/${MYSQL_DATABASE}", "--spring.datasource.username=${MYSQL_USERNAME}", "--spring.datasource.password=${MYSQL_PASSWORD}"]