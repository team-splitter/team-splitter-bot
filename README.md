# team-splitter-bot

## Setup

- Install Docker and Enable Kubernetes
- Install ArgoCD to K8s (https://argo-cd.readthedocs.io/en/stable/getting_started/)
- Reset Password in ArgoCD
- Make sure for mysql, following folder `/Users/maximus/mysql_data` is available for Persistent Volume (https://github.com/team-splitter/team-splitter-bot/blob/main/.manifests/kubernetes/prod/mysql-pv.yaml)
- Add app to ArgoCD
  ```
  kubectl apply -f https://github.com/team-splitter/team-splitter-bot/blob/main/.manifests/kubernetes/application_prod.yaml
  ```

## ArgoCD port forwarding
```
kubectl port-forward svc/argocd-server -n argocd 8080:443
```

## k8s mysql apply dump
```
source .env

mysql_pod=$(kubectl get pods -n team-splitter | grep mysql | cut -d' ' -f1)
cat backups/file | kubectl exec --stdin --tty $mysql_pod -n team-splitter -- /usr/bin/mysql -uroot -p${MYSQLDB_ROOT_PASSWORD} team-splitter
```

## Crontab
```
0 12 * * * sh /Users/lotus/team-splitter/dbdump.sh >> /Users/lotus/team-splitter/dbdump.log 2>&1
0 12 * * * sh /Users/lotus/team-splitter/generate_player_stats.sh >> /Users/lotus/team-splitter/generate_player_stats.log 2>&1
```

## Mac is awake
```
caffeinate -d
```

## IPS port forwarding 
Go to xfinity app -> WiFi Equipment-> Advanced Settings -> Port Forwarding
9000 -> FE and Bakcend
30360 - Mysql DB

*** Docker
Start mysql server on docker (Not used on PROD)
```shell
docker stop mysql
docker rm mysql

docker run --name mysql -d \
  -p 3306:3306 \
  -e MYSQL_ROOT_PASSWORD=changeit \
  -e MYSQL_DATABASE=team-splitter \
  -u root \
  --restart unless-stopped \
  -v mysql:/var/lib/mysql \
  mysql:8
```

Apply backup to mysql server
```shell
cat backup.sql | docker exec -i  mysql /usr/bin/mysql -u root --password=changeit team-splitter
```

## Docker config as k8s secret
```shell
kubectl create secret generic regcred \
    --from-file=.dockerconfigjson=docker_config.json \           
    --type=kubernetes.io/dockerconfigjson
```