FROM eclipse-temurin:21-jdk-jammy
ARG JAR_FILE=target/mym_dental_commerce-0.0.1.jar
COPY ${JAR_FILE} mym_dental_commerce.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "mym_dental_commerce.jar"]