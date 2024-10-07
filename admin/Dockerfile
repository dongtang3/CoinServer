FROM openjdk:21-jdk-alpine
#Create a directory in the container
WORKDIR /app
#Copy the jar file to the container
COPY target/admin-1.0-SNAPSHOT.jar /app/admin-1.0-SNAPSHOT.jar
#Exposed port
EXPOSE 8081
#Run the jar file
ENTRYPOINT ["java", "-jar", "/app/admin-1.0-SNAPSHOT.jar"]


