#!/bin/bash
# validate postgres startup

echo "sleeping for 5 seconds during postgres boot..."
sleep 5
PGPASSWORD="${DATABASE_USER_PASSWORD:-dbpwd}" \
  psql --username "${DATABASE_USER:-dbuser}" -d "${DATABASE_NAME:-javatemplate_db}" \
       -c "SELECT VERSION(); SELECT NOW()"
