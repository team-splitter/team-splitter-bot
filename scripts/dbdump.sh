#!/bin/bash

echo "$(date) DB dump"

SHELL=/bin/sh
PATH=/usr/local/sbin:/usr/local/bin:/sbin:/bin:/usr/sbin:/usr/bin

cd /Users/maximus/team-splitter
source .env

mysql_pod=$(kubectl get pods -n team-splitter | grep mysql | cut -d' ' -f1)
kubectl exec --stdin --tty $mysql_pod -n team-splitter -- /usr/bin/mysqldump -uroot -p${MYSQLDB_ROOT_PASSWORD} team-splitter > backups/$(date '+%Y-%m-%d')_$(date '+%s').sql

echo "$(date) DB dump complete"
