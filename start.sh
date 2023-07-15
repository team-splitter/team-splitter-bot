source .env

echo "test skip ci 1"

docker stop mysql
docker rm mysql

docker run --name mysql -d \
  --network team-splitter-net \
  -p 3306:3306 \
  -e MYSQL_ROOT_PASSWORD=$MYSQLDB_ROOT_PASSWORD \
  -e MYSQL_DATABASE=team-splitter \
  -u root \
  --restart unless-stopped \
  -v mysql:/var/lib/mysql \
  mysql:8

docker stop bot
docker rm bot
docker run --name bot -d \
  --network  team-splitter-net \
  --env-file ./.env \
  --restart unless-stopped \
  -e SPRING_APPLICATION_JSON='{
              "telegram.bot.token": "${TELEGRAM_BOT_TOKEN}",
               "team-splitter.db.mysql.url"  : "jdbc:mysql://mysql:${MYSQLDB_DOCKER_PORT}/${MYSQLDB_DATABASE}?allowPublicKeyRetrieval=true&useSSL=false",
               "team-splitter.db.mysql.user" : "${MYSQLDB_USER}",
               "team-splitter.db.mysql.password" : "${MYSQLDB_ROOT_PASSWORD}",
               "spring.jpa.properties.hibernate.dialect" : "org.hibernate.dialect.MySQL5InnoDBDialect"
             }' \
  ghcr.io/team-splitter/team-splitter-bot:1.13

  docker stop backend
  docker rm backend
  docker run --name backend -d \
    -p 8080:8080 \
    --network team-splitter-net \
    --env-file ./.env \
    --restart unless-stopped \
    -e SPRING_APPLICATION_JSON='{
                 "team-splitter.db.mysql.url"  : "jdbc:mysql://mysql:${MYSQLDB_DOCKER_PORT}/${MYSQLDB_DATABASE}?allowPublicKeyRetrieval=true&useSSL=false",
                 "team-splitter.db.mysql.user" : "${MYSQLDB_USER}",
                 "team-splitter.db.mysql.password" : "${MYSQLDB_ROOT_PASSWORD}",
                 "spring.jpa.properties.hibernate.dialect" : "org.hibernate.dialect.MySQL5InnoDBDialect"
               }' \
    ghcr.io/team-splitter/team-splitter-server:1.13

docker stop frontend
docker rm frontend
docker run --name frontend -d \
  -p 9000:80 \
  --network team-splitter-net \
  --env-file ./.env \
  --restart unless-stopped \
  ghcr.io/team-splitter/team-splitter-ui:1.7
