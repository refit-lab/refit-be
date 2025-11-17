FROM eclipse-temurin:21-jre

WORKDIR /app

# JAR는 외부에서 마운트하거나 CI에서 복사
COPY app.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar", "--spring.profiles.active=${SPRING_PROFILES_ACTIVE:-prod}"]