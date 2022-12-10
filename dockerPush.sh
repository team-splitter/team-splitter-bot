#!/bin/bash

if [ $# -le 0 ]
then
  echo "Missing version argument";
  exit 1;
else
  version=$1;
fi

echo "Pushing image mukhanovmax/team-splitter-bot:${version}"
docker push mukhanovmax/team-splitter-bot:$version

echo "Pushing image mukhanovmax/team-splitter-server:${version}"
docker push mukhanovmax/team-splitter-server:$version