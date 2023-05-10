#----------
# more images see https://hub.docker.com/_/microsoft-java-jdk
# If you are considering to use newest image, update runtime platform and local java version as well
FROM mcr.microsoft.com/java/jdk:11u11-zulu-ubuntu as build

WORKDIR /app

# lets copy minimum of gradle wrapper files and run gradle wrapper
# with a sideefect-less command so that
# docker will create single layer reused later non with just downloaded gradle binaries
COPY ./main/gradlew .
COPY ./main/gradle ./gradle
RUN  ./gradlew --stop


# Copy all of the files to start core part of the build
COPY . .

WORKDIR /app/main
RUN chmod +x ./gradlew
RUN REVISION=$(cat ../.version) && \
    ./gradlew build -Drevision=$REVISION -x test

# lets go where already prepared fat jar is located and extract all layers
# to temporal folder
WORKDIR /dist

RUN REVISION=$(cat /app/.version) && \
    java -Djarmode=layertools -jar /app/main/build/libs/customers-$REVISION.jar extract

#----------
# list of images is located @ https://hub.docker.com/_/microsoft-java-jre-headless
# If you are considering to use newest image, update compilation platform and local java version as well
FROM mcr.microsoft.com/java/jre-headless:17u1-zulu-alpine as dist

# Copy well known (defined by Spring Boot) jar layers
# more: https://www.baeldung.com/docker-layers-spring-boot#3-dockerfile-configuration
# why RUN true? see # see https://github.com/moby/moby/issues/37965#issuecomment-426853382
# feel free to remove RUN true and' if build is green, stay without the 'RUN true' statement
COPY --from=build /dist/dependencies/ /app/
RUN true
COPY --from=build /dist/snapshot-dependencies/ /app/
RUN true
COPY --from=build /dist/spring-boot-loader/ /app/
RUN true
COPY --from=build /dist/application/ /app/

# # The application needs some configuration
# ENV SPRING_PROFILES_ACTIVE=prod
# # TODO Right now database name is hardcoded, but should be provided via configuration
# ENV APP_DB_HOST: db

# # the application os fat jar so only one file is required to run the app
# COPY --from=build /app/main/bin/build/libs/*.jar /app/


# https://www.baeldung.com/spring-boot-docker-images#3-creating-the-docker-image



# # we we require application insight agent when app is run
# # let's copy agent from expected location and set properly jvm option
# COPY --from=build /app/applicationinsights /app/
# ENV _JAVA_OPTIONS="-javaagent:/app/applicationinsights-agent-3.1.0.jar -Xmx512m -XX:+UseG1GC"

# # fat-jar name is well-known, let's simplify the name so that we can use simpler name in ENTRYPOINT
# RUN mv /app/host-$REVISION.jar /app/host.jar
# EXPOSE 8080
# ENTRYPOINT ["java","-jar","/app/host.jar"]