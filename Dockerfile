#
# Build stage
#
FROM maven:3.6.3-openjdk-15-slim AS build
COPY src /home/circleci/src
COPY pom.xml /home/circleci
RUN mvn -f /home/circleci/pom.xml clean package

#
# Package stage
#
FROM openjdk:15-jdk-slim
COPY --from=build /home/circleci/target/test-circleci-0.0.1.jar /usr/local/lib/test-circleci-0.0.1.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","/usr/local/lib/test-circleci-0.0.1.jar"]