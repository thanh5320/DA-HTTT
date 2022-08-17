FROM openjdk:11.0-jdk
ENV TZ "Asia/Ho_Chi_Minh"
RUN rm -rf /var/cache/apk/*
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} /usr/app/
WORKDIR /usr/app
EXPOSE 8082
ENTRYPOINT exec java $JAVA_OPTS -jar $JAVA_ARGS movie_review-0.0.1-SNAPSHOT.jar