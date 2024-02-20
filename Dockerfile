#
# Build Stage
#
FROM maven:3.9.6-amazoncorretto-21 AS build
COPY . .
WORKDIR /
RUN mvn clean package -DskipTests

#
# Package Stage
#
FROM openjdk:21
COPY --from=build /target/reactive-secure-api.jar reactive-secure-api.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "reactive-secure-api.jar"]
