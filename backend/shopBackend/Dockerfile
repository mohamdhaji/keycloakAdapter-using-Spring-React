FROM openjdk:8-alpine
COPY /target/shop-0.0.1-SNAPSHOT.jar shop.jar
EXPOSE 9090
ENTRYPOINT ["java","-jar","shop.jar"]