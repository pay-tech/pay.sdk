#!/bin/bash

SCRIPT_DIR="$(dirname "${BASH_SOURCE:-$0}")"
ROOT_DIR=`python -c 'import os,sys;print os.path.realpath(sys.argv[1])' "$SCRIPT_DIR/../../.."`

cd $ROOT_DIR

# detect gradle command
GRADLE=`which gradle`
GRADLE="${GRADLE:-$ROOT_DIR/gradlew}"

"$GRADLE" clean \
          :sdk:assembleRelease

 sleep 1s # wait the file being generated

 "$GRADLE" :sdk:dist \
           -PVARIANT=Release

 "$GRADLE" :sdk:publish \
           -PVARIANT=Release