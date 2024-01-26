FROM openjdk:21 AS TEMP_BUILD_IMAGE
RUN microdnf install findutils
ENV APP_HOME=/usr/app/service-discovery
WORKDIR $APP_HOME
COPY build.gradle settings.gradle gradlew $APP_HOME
COPY src $APP_HOME/src
COPY gradle $APP_HOME/gradle
RUN ./gradlew build

FROM openjdk:21
ENV ARTIFACT_NAME=service.discovery*.jar
ENV APP_HOME=/usr/app/service-discovery
WORKDIR $APP_HOME
COPY --from=TEMP_BUILD_IMAGE $APP_HOME/build/libs/$ARTIFACT_NAME app.jar
CMD ["java","-jar","app.jar"]
