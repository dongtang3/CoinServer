@echo off


aws ecr get-login-password --region us-east-1 | docker login --username AWS --password-stdin 853228521229.dkr.ecr.us-east-1.amazonaws.com

:: Define Variables
set AWS_ACCOUNT_ID=853228521229
set AWS_REGION=us-east-1
set SERVICE_PREFIX=coinserver/

:: Maven Build
mvn clean package

:: Build and Tag Docker Images
docker build -t account-service ./account
docker tag account-service:latest %AWS_ACCOUNT_ID%.dkr.ecr.%AWS_REGION%.amazonaws.com/%SERVICE_PREFIX%account-service:latest

docker build -t admin-service ./admin
docker tag admin-service:latest %AWS_ACCOUNT_ID%.dkr.ecr.%AWS_REGION%.amazonaws.com/%SERVICE_PREFIX%admin-service:latest

docker build -t eureka-service ./discovery
docker tag eureka-service:latest %AWS_ACCOUNT_ID%.dkr.ecr.%AWS_REGION%.amazonaws.com/%SERVICE_PREFIX%eureka-service:latest

docker build -t gateway-service ./gateway
docker tag gateway-service:latest %AWS_ACCOUNT_ID%.dkr.ecr.%AWS_REGION%.amazonaws.com/%SERVICE_PREFIX%gateway-service:latest

docker build -t exchange-service ./exchange
docker tag exchange-service:latest %AWS_ACCOUNT_ID%.dkr.ecr.%AWS_REGION%.amazonaws.com/%SERVICE_PREFIX%exchange-service:latest

docker build -t market-service ./market
docker tag market-service:latest %AWS_ACCOUNT_ID%.dkr.ecr.%AWS_REGION%.amazonaws.com/%SERVICE_PREFIX%market-service:latest

docker build -t statistics-service ./statistics
docker tag statistics-service:latest %AWS_ACCOUNT_ID%.dkr.ecr.%AWS_REGION%.amazonaws.com/%SERVICE_PREFIX%statistics-service:latest

:: Push Docker Images to ECR
docker push %AWS_ACCOUNT_ID%.dkr.ecr.%AWS_REGION%.amazonaws.com/%SERVICE_PREFIX%account-service:latest
docker push %AWS_ACCOUNT_ID%.dkr.ecr.%AWS_REGION%.amazonaws.com/%SERVICE_PREFIX%admin-service:latest
docker push %AWS_ACCOUNT_ID%.dkr.ecr.%AWS_REGION%.amazonaws.com/%SERVICE_PREFIX%eureka-service:latest
docker push %AWS_ACCOUNT_ID%.dkr.ecr.%AWS_REGION%.amazonaws.com/%SERVICE_PREFIX%gateway-service:latest
docker push %AWS_ACCOUNT_ID%.dkr.ecr.%AWS_REGION%.amazonaws.com/%SERVICE_PREFIX%exchange-service:latest
docker push %AWS_ACCOUNT_ID%.dkr.ecr.%AWS_REGION%.amazonaws.com/%SERVICE_PREFIX%market-service:latest
docker push %AWS_ACCOUNT_ID%.dkr.ecr.%AWS_REGION%.amazonaws.com/%SERVICE_PREFIX%statistics-service:latest
