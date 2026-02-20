FROM eclipse-temurin:25-jdk-alpine
ARG JAR_FILE=target/demo-0.0.1.jar
COPY ${JAR_FILE} app_proyectobackendjava.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app_proyectobackendjava.jar"]