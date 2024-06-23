# Stage 1: Build the application
FROM public.ecr.aws/docker/library/maven:3.9.5 AS build
WORKDIR /app
COPY pom.xml /app
RUN mvn dependency:resolve
COPY . /app
RUN mvn clean
RUN mvn package -DskipTests -X

# Stage 2: Create the Docker image
FROM public.ecr.aws/docker/library/openjdk:11
COPY --from=build /app/target/*.jar /app.jar
EXPOSE 8080
CMD ["java", "-jar", "/app.jar"]
