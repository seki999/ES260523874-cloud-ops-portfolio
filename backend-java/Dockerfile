FROM eclipse-temurin:21-jdk AS build
WORKDIR /app
COPY src ./src
RUN find src/main/java -name "*.java" > sources.txt && javac -encoding UTF-8 -d out @sources.txt

FROM eclipse-temurin:21-jre
WORKDIR /app
COPY --from=build /app/out ./out
EXPOSE 8080
CMD ["java", "-cp", "out", "com.es260523874.cloudops.CloudOpsApplication"]

