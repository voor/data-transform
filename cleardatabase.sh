#!/usr/bin/env bash

mysql -u root -pchangeme <<-EOF
DROP DATABASE movielens;
CREATE DATABASE movielens;
GRANT ALL PRIVILEGES ON movielens.* TO 'movies'@'172.17.0.4' \
IDENTIFIED BY 'movies';
FLUSH PRIVILEGES;
EOF

if [ $? -eq 0 ]; then
  printf "Successful.\n"
fi
