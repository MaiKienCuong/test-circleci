FROM adoptopenjdk/openjdk11:jdk-11.0.14.1_1-alpine-slim AS build
WORKDIR /home/circleci/
COPY .mvn ./.mvn
COPY mvnw pom.xml ./
COPY /root/.m2/repository /root/.m2/repository
COPY src ./src
RUN chmod +x mvnw && sed -i 's/\r$//' mvnw && ./mvnw clean package -Dmaven.test.skip=true

FROM adoptopenjdk/openjdk11:jdk-11.0.14.1_1-alpine-slim
COPY --from=build /home/circleci/target/test-circleci-0.0.1.jar /usr/local/lib/test-circleci-0.0.1.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","/usr/local/lib/test-circleci-0.0.1.jar"]