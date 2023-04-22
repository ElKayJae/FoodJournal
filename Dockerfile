FROM node:19 AS angularBuilder

WORKDIR /app

COPY client .

RUN npm i -g @angular/cli
RUN npm i
RUN ng build 



FROM maven:3.9.0-eclipse-temurin-19 AS jarBuilder

WORKDIR /app

COPY server/mvnw .
COPY server/mvnw.cmd .
COPY server/pom.xml .
COPY server/src ./src
COPY --from=angularBuilder /app/dist/client ./src/main/resources/static

RUN mvn package -Dmaven.test.skip=true



FROM eclipse-temurin:19-jre

WORKDIR /app

COPY --from=jarBuilder app/target/server-0.0.1-SNAPSHOT.jar server.jar

ENV PORT=8080

# what port the program is listening to
EXPOSE ${PORT}

ENTRYPOINT java -Dserver.port=${PORT} -jar server.jar

# docker build -t elkayjae/foodjournal:v0 .
# docker run -d -p  8080:8080 elkayjae/foodjournal:v0
