FROM maven:3.6.2-jdk-11 AS builder
COPY src /usr/src/app/src
COPY pom.xml /usr/src/app
RUN mvn -f /usr/src/app/pom.xml clean package

FROM adoptopenjdk:11-jre-hotspot
RUN mkdir /opt/app
COPY --from=builder /usr/src/app/target/user-details-0.0.1-SNAPSHOT.jar /opt/app/app.jar
EXPOSE 8082
ENTRYPOINT [ "java", "-jar", "/opt/app/app.jar" ]