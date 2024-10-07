FROM openjdk:21-jdk-alpine
#Create a directory in the container
WORKDIR /app
#Copy the jar file to the container
COPY target/account-1.0-SNAPSHOT.jar /app/account-1.0-SNAPSHOT.jar
#Exposed port
EXPOSE 8080
#Run the jar file
ENTRYPOINT ["java", "-jar", "/app/account-1.0-SNAPSHOT.jar"]


