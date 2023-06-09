# team-splitter-bot


Start mysql server on docker
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