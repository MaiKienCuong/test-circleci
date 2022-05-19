FROM adoptopenjdk/openjdk11:jdk-11.0.14.1_1-alpine-slim

EXPOSE 8080

COPY /target/test-circleci-0.0.1.jar /usr/local/lib/test-circleci-0.0.1.jar

ENTRYPOINT ["java","-jar","/usr/local/lib/test-circleci-0.0.1.jar"]