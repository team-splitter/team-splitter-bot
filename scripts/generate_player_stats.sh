#!/bin/bash

echo "$(date) Generating Player Stats"

SHELL=/bin/sh
PATH=/usr/local/sbin:/usr/local/bin:/sbin:/bin:/usr/sbin:/usr/bin

cd /Users/maximus/team-splitter
source .env

mysql_pod=$(kubectl get pods -n team-splitter | grep mysql | cut -d' ' -f1)
cat generatePlayerStats.sql | kubectl exec --stdin --tty $mysql_pod -n team-splitter -- /usr/bin/mysql -uroot -p${MYSQLDB_ROOT_PASSWORD} team-splitter

echo "$(date) Player Stats Generation Completed"
