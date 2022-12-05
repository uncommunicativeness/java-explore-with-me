#!/bin/bash

CUR_DIR=$(pwd)

MODULES="ewm-service
        stats-server"

function build() {
  MOD_DIR=$1

  cd "$MOD_DIR" || exit

  echo "Build module: $MOD_DIR"
  docker build -t "uncommunicativeness/explore-with-me-$MOD_DIR" .

  echo "Build module: $MOD_DIR success"

  cd "$CUR_DIR" || exit
}

function build_all() {
  for mod_dir in ${1}; do
    build "${mod_dir}"
  done
}

docker compose down

mvn clean install -Dmaven.test.skip

build_all "${MODULES}"

echo "All module build success"

docker compose up -d