FROM openjdk:23-jdk

WORKDIR /app

COPY target/dscommerce-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 8080

COPY src/main/resources/application-prod.properties /app/config/
ENV SPRING_CONFIG_LOCATION=classpath:/,file:/app/config/

CMD [ "java","-jar","app.jar" ]