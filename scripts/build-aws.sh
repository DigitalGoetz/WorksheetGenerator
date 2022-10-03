#!/bin/bash

IMAGE=digitalgoetz/worksheet-api
TAG=1.0.0

docker build -f Dockerfile.api -t $IMAGE:$TAG .
docker push $IMAGE:$TAG
