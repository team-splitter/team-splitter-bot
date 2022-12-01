#!/bin/bash

if [ $# -le 0 ]
then
  echo "Missing version argument";
  exit 1;
else
  version=$1;
fi

echo $version

mvn clean package

docker build -t mukhanovmax/team-splitter-bot:$version .