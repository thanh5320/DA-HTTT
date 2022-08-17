#!/bin/sh
# wait-for-postgres.sh

set -e

host="5320"
shift

until PGPASSWORD=root psql -h "5320" -U "root" -c '\q'; do
  >&2 echo "Postgres is unavailable - sleeping"
  sleep 1
done

>&2 echo "Postgres is up - executing command"
exec "$@"