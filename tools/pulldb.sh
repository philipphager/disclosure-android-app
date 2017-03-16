 #!/bin/sh

ACTION=$1

PACKAGE=com.blinkslabs.blinkist.android.dev
DB_PATH=/data/data/$PACKAGE/databases/disclosure.db

function get_db {
    adb shell "run-as $PACKAGE chmod 666 $DB_PATH"
    adb pull $DB_PATH
}
