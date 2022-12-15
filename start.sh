source .env

docker stop mysql
docker rm mysql

docker run --name mysql -d \
  --network springmysql-net \
  -p 3306:3306 \
  -e MYSQL_ROOT_PASSWORD=$MYSQLDB_ROOT_PASSWORD \
  -e MYSQL_DATABASE=team-splitter \
  -u root \
  --restart unless-stopped \
  -v mysql:/var/lib/mysql \
  mysql:8

docker stop bot
docker rm bot
docker run --name app -d \
  --network springmysql-net \
  --env-file ./.env \
  -e SPRING_APPLICATION_JSON='{
              "telegram.bot.token": "${TELEGRAM_BOT_TOKEN}",
               "team-splitter.db.mysql.url"  : "jdbc:mysql://mysql:${MYSQLDB_DOCKER_PORT}/${MYSQLDB_DATABASE}?allowPublicKeyRetrieval=true&useSSL=false",
               "team-splitter.db.mysql.user" : "${MYSQLDB_USER}",
               "team-splitter.db.mysql.password" : "${MYSQLDB_ROOT_PASSWORD}",
               "spring.jpa.properties.hibernate.dialect" : "org.hibernate.dialect.MySQL5InnoDBDialect"
             }' \
  mukhanovmax/team-splitter-bot:1.5

  docker stop backend
  docker rm backend
  docker run --name backend -d \
    -p80:8080 \
    --network springmysql-net \
    --env-file ./.env \
    -e SPRING_APPLICATION_JSON='{
                 "team-splitter.db.mysql.url"  : "jdbc:mysql://mysql:${MYSQLDB_DOCKER_PORT}/${MYSQLDB_DATABASE}?allowPublicKeyRetrieval=true&useSSL=false",
                 "team-splitter.db.mysql.user" : "${MYSQLDB_USER}",
                 "team-splitter.db.mysql.password" : "${MYSQLDB_ROOT_PASSWORD}",
                 "spring.jpa.properties.hibernate.dialect" : "org.hibernate.dialect.MySQL5InnoDBDialect"
               }' \
    mukhanovmax/team-splitter-server:1.5
