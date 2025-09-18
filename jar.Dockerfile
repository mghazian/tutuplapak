FROM public.ecr.aws/docker/library/eclipse-temurin:21-jdk-alpine
WORKDIR /app

COPY ./build/libs/tutuplapak-0.0.1-SNAPSHOT.jar coffeeteam-tutuplapak.jar

RUN addgroup -S spring && adduser -S spring -G spring
USER spring:spring

CMD ["java", "-jar", "coffeeteam-tutuplapak.jar", "$ARGS"]