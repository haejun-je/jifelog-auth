#!/bin/sh
set -eu

need_file () {
  if [ ! -f "$1" ]; then
    echo "Missing secret file: $1" >&2
    exit 1
  fi
}

need_file /etc/secrets/DB_HOST
need_file /etc/secrets/DB_NAME
need_file /etc/secrets/DB_USER
need_file /etc/secrets/DB_PASSWORD
need_file /etc/secrets/REDIS_HOST
need_file /etc/secrets/REDIS_PASSWORD
need_file /etc/secrets/MAIL_API_KEY
need_file /etc/secrets/JWT_SECRET

export DB_HOST="$(tr -d '\r\n' <  /etc/secrets/DB_HOST)"
export DB_NAME="$(tr -d '\r\n' <  /etc/secrets/DB_NAME)"
export DB_USER="$(tr -d '\r\n' <  /etc/secrets/DB_USER)"
export DB_PASSWORD="$(tr -d '\r\n' <  /etc/secrets/DB_PASSWORD)"
export REDIS_HOST="$(tr -d '\r\n' <  /etc/secrets/REDIS_HOST)"
export REDIS_PASSWORD="$(tr -d '\r\n' <  /etc/secrets/REDIS_PASSWORD)"
export MAIL_API_KEY="$(tr -d '\r\n' <  /etc/secrets/MAIL_API_KEY)"
export JWT_SECRET="$(tr -d '\r\n' <  /etc/secrets/JWT_SECRET)"

exec java -jar /app/app.jar