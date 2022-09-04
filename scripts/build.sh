#!/bin/bash

IMAGE=digitalgoetz/worksheet
TAG=1.0.0

docker build -t $IMAGE:$TAG .
docker push $IMAGE:$TAG
