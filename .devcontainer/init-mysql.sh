#!/bin/bash

set -e

echo "Iniciando MySQL..."
service mysql start

echo "Aguardando MySQL ficar pronto..."

for tentativa in $(seq 1 30); do
    if mysqladmin ping --silent; then
        echo "MySQL pronto."
        break
    fi
    sleep 1
done

if ! mysqladmin ping --silent; then
    echo "Erro: MySQL não respondeu a tempo." >&2
    exit 1
fi

echo "Configurando banco..."

mysql <<EOF
CREATE DATABASE IF NOT EXISTS ecommerce
CHARACTER SET utf8mb4
COLLATE utf8mb4_unicode_ci;

CREATE USER IF NOT EXISTS 'rafael'@'%' IDENTIFIED BY 'q1w2e3';

GRANT ALL PRIVILEGES ON ecommerce.* TO 'rafael'@'%';

FLUSH PRIVILEGES;
EOF

echo "MySQL configurado com sucesso!"
