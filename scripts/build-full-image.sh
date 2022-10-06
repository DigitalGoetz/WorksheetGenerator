#!/bin/bash

IMAGE=digitalgoetz/worksheet
TAG=1.0.1

docker build -f Dockerfile.all -t $IMAGE:$TAG .
docker push $IMAGE:$TAG
