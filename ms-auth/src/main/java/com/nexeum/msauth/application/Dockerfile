FROM openjdk:21-jdk-slim

LABEL maintainer="srandres1803@gmail.com"

VOLUME /tmp

EXPOSE 8080

ARG JAR_FILE=build/libs/ms-auth-0.0.1-SNAPSHOT.jar

ADD ${JAR_FILE} ms-auth.jar

ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/ms-auth.jar"]