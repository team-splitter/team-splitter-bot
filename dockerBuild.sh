#!/bin/bash

if [ $# -le 0 ]
then
  echo "Missing version argument";
  exit 1;
else
  version=$1;
fi

echo "Building version "$version


mvn clean package


echo "Building image ghcr.io/team-splitter/team-splitter-bot:${version}"
docker build -t ghcr.io/team-splitter/team-splitter-bot:$version ./team-splitter-app/

echo "Building image ghcr.io/team-splitter/team-splitter-server:${version}"
docker build -t ghcr.io/team-splitter/team-splitter-server:$version ./team-splitter-server/