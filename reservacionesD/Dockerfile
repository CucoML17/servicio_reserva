FROM eclipse-temurin:17-jre-focal
ARG JAR_FILE=target/reservaciones-0.0.1-SNAPSHOT.jar
COPY ${JAR_FILE} app_servicio_reserva.jar
EXPOSE 3012
ENTRYPOINT ["java", "-jar", "app_servicio_reserva.jar"]