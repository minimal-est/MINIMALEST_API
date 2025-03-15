FROM eclipse-temurin:17-jre-jammy
WORKDIR /app

ENV SPRING_PROFILES_ACTIVE=$SPRING_PROFILES_ACTIVE
ENV MYSQL_URL=$MYSQL_URL
ENV MYSQL_PORT=$MYSQL_PORT
ENV MYSQL_DB_NAME=$MYSQL_DB_NAME
ENV MYSQL_USERNAME=$MYSQL_USERNAME
ENV MYSQL_PASSWORD=$MYSQL_PASSWORD
ENV AWS_S3_ACCESS_KEY=$AWS_S3_ACCESS_KEY
ENV AWS_S3_SECRET_KEY=$AWS_S3_SECRET_KEY
ENV AWS_S3_REGION=$AWS_S3_REGION
ENV AWS_S3_BUCKET=$AWS_S3_BUCKET
ENV JWT_SECRET_KEY=$JWT_SECRET_KEY

COPY build/libs/*.jar app.jar

ENTRYPOINT ["java", "-jar", "app.jar"]
