docker run --name mysql -d \
    -p 3306:3306 \
    -e MYSQL_ROOT_PASSWORD=pass \
    -e MYSQL_DATABASE=team-splitter \
    -u root \
    --restart unless-stopped \
    -v mysql:/var/lib/mysql \
    mysql:8