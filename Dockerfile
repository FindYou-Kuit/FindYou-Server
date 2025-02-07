FROM amazoncorretto:17
EXPOSE 9001
COPY ./build/libs/*.jar ./app.jar

ENV SPRING_PROFILES_ACTIVE=dev
ENTRYPOINT ["java", "-jar", "app.jar"]