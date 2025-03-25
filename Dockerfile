FROM eclipse-temurin:17-jre-jammy
WORKDIR /app

ARG MYSQL_URL
ARG MYSQL_PORT
ARG MYSQL_DB_NAME
ARG MYSQL_USERNAME
ARG MYSQL_PASSWORD
ARG AWS_S3_ACCESS_KEY
ARG AWS_S3_SECRET_KEY
ARG AWS_S3_REGION
ARG AWS_S3_BUCKET
ARG JWT_SECRET_KEY
ARG OAUTH_GOOGLE_CLIENT_ID
ARG OAUTH_GOOGLE_CLIENT_SECRET
ARG OAUTH_GOOGLE_REDIRECT_URI

ENV SPRING_PROFILES_ACTIVE=prod
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
ENV OAUTH_GOOGLE_CLIENT_ID=$OAUTH_GOOGLE_CLIENT_ID
ENV OAUTH_GOOGLE_CLIENT_SECRET=$OAUTH_GOOGLE_CLIENT_SECRET
ENV OAUTH_GOOGLE_REDIRECT_URI=$OAUTH_GOOGLE_REDIRECT_URI

COPY build/libs/*.jar app.jar

ENTRYPOINT ["java", "-Duser.timezone=Asia/Seoul", "-jar", "app.jar"]
