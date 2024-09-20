FROM openjdk:17-jdk-alpine
#Create a directory in the container
WORKDIR /app
#Copy the jar file to the container
COPY target/exchange-1.0-SNAPSHOT.jar /app/exchange-1.0-SNAPSHOT.jar
#Exposed port
EXPOSE 8082
#Run the jar file
ENTRYPOINT ["java", "-jar", "/app/exchange-1.0-SNAPSHOT.jar"]


