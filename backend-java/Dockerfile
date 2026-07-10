FROM eclipse-temurin:21-jdk AS build
WORKDIR /app
ADD https://repo1.maven.org/maven2/com/mysql/mysql-connector-j/8.4.0/mysql-connector-j-8.4.0.jar /app/mysql-connector-j.jar
COPY src ./src
RUN find src/main/java -name "*.java" > sources.txt && javac -encoding UTF-8 -cp mysql-connector-j.jar -d out @sources.txt

FROM eclipse-temurin:21-jre
WORKDIR /app
COPY --from=build /app/out ./out
COPY --from=build /app/mysql-connector-j.jar ./mysql-connector-j.jar
EXPOSE 8080
CMD ["java", "-cp", "out:mysql-connector-j.jar", "com.es260523874.cloudops.CloudOpsApplication"]
