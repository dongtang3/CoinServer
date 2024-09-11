CoinServer Project
Overview
CoinServer is a distributed microservices-based project built with Spring Boot 3.0 and Eureka. The project follows a client-server architecture where the discovery module serves as the service discovery server, and all other modules (like account, admin, exchange, gateway, market, and statistics) act as service clients.


Project Structure
discovery: This module is the Eureka server responsible for service discovery and registration.
account, admin, exchange, gateway, market, statistics: These modules are the microservices that register themselves with the Eureka server and communicate with each other via service discovery.


Prerequisites
Java 17 or above
Maven 3.8.x or above
Spring Boot 3.0
Eureka (Service Discovery)

Getting Started
1. Start the Discovery Server
   The discovery server is the core of this distributed system. It must be up and running before launching any other service clients. http://localhost:8761/

2. Start the Microservice Clients
   Once the Eureka server is running, you can start the other modules, which act as service clients. Each of these clients will register themselves with the Eureka server.(e.g., admin, exchange, gateway, market, and statistics).


http://localhost:8080/swagger-ui/