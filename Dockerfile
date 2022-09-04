FROM maven:3-jdk-11-slim as API_BUILDER

WORKDIR /build
COPY ./api/pom.xml /build/pom.xml
RUN mvn verify clean --fail-never
COPY ./api /build/
RUN mvn install

FROM node:18.8.0-alpine3.16 as UI_BUILDER

WORKDIR /build
COPY ./ui/ /build/
RUN npm install && npm run build

FROM eclipse-temurin:11.0.16.1_1-jre-jammy
WORKDIR /application
COPY --from=API_BUILDER /build/target/WorksheetGenerator-0.0.1-SNAPSHOT-fatjar.jar /application/worksheet.jar
COPY --from=UI_BUILDER /build/build/ /application/ui/

CMD ["java", "-jar", "/application/worksheet.jar"]
