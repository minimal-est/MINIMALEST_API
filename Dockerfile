FROM eclipse-temurin:17-jre-jammy
WORKDIR /app

ENV SPRING_PROFILES_ACTIVE=prod

COPY build/libs/*.jar app.jar

ENTRYPOINT ["java", "-jar", "app.jar"]
