FROM eclipse-temurin:17-jdk-alpine
VOLUME /tmp
COPY target/CreditS-1.0.jar app.jar
CMD ["java","-jar","/app.jar"]
