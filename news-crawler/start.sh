#!/usr/bin/env bash
DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" >/dev/null 2>&1 && pwd )"

cd $DIR || echo "DIR not found"

DATE=$(date +"%Y-%m-%d")
PID_FOLDER="$DIR/pid/"

YESTERDAY_ARG=$1
if [ "$YESTERDAY_ARG" == "yesterday" ]; then
    DATE=$(date -d 'yesterday' +"%Y-%m-%d")
fi

DATA_FOLDER="$DIR/data/$DATE"
LOG_FILE="$DIR/logs/$DATE.log"

echo $DATE
echo $DATA_FOLDER
echo $LOG_FILE

mkdir -p "$DIR/data/$DATE"
mkdir -p "$DIR/logs/"
mkdir -p "$DIR/pid/"

if [ "$YESTERDAY_ARG" == "yesterday" ]; then
    nohup python3 $DIR/main.py $DATE >> $LOG_FILE 2>&1 & echo $! >> $PID_FOLDER/$DATE.pid
else
    source my_venv/bin/activate
    nohup python3 $DIR/main.py
fi
