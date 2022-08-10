FROM maven:3-jdk-11-slim

COPY . /application/

WORKDIR /application

RUN mvn clean install

CMD ["java", "-jar", "./target/WorksheetGenerator-0.0.1-SNAPSHOT-fatjar.jar"]