#!/bin/bash

# Set variables
REPOSITORY_PREFIX="public.ecr.aws/r5h2j0g1"
IMAGE_TAG="latest"

# Tag and push images
docker tag account-service:$IMAGE_TAG $REPOSITORY_PREFIX/account-service:$IMAGE_TAG
docker push $REPOSITORY_PREFIX/account-service:$IMAGE_TAG

docker tag admin-service:$IMAGE_TAG $REPOSITORY_PREFIX/admin-service:$IMAGE_TAG
docker push $REPOSITORY_PREFIX/admin-service:$IMAGE_TAG

docker tag eureka-service:$IMAGE_TAG $REPOSITORY_PREFIX/eureka-service:$IMAGE_TAG
docker push $REPOSITORY_PREFIX/eureka-service:$IMAGE_TAG

docker tag exchange-service:$IMAGE_TAG $REPOSITORY_PREFIX/exchange-service:$IMAGE_TAG
docker push $REPOSITORY_PREFIX/exchange-service:$IMAGE_TAG

docker tag gateway-service:$IMAGE_TAG $REPOSITORY_PREFIX/gateway-service:$IMAGE_TAG
docker push $REPOSITORY_PREFIX/gateway-service:$IMAGE_TAG

docker tag market-service:$IMAGE_TAG $REPOSITORY_PREFIX/market-service:$IMAGE_TAG
docker push $REPOSITORY_PREFIX/market-service:$IMAGE_TAG

docker tag statistics-service:$IMAGE_TAG $REPOSITORY_PREFIX/statistics-service:$IMAGE_TAG
docker push $REPOSITORY_PREFIX/statistics-service:$IMAGE_TAG

echo "Deployment completed successfully!"
