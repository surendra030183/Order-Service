# Start with a base image containing Java runtime
FROM openjdk:11
# Make port 8080 available to the world outside this container
EXPOSE 8081
ADD target/orders.jar orders.jar
ENTRYPOINT ["java","-jar","orders.jar"]