FROM eclipse-temurin:17-jdk-alpine
VOLUME /tmp
COPY target/CreditS-0.0.9.jar app.jar
CMD ["java","-jar","/app.jar"]