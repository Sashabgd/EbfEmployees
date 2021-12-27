#!/bin/bash
ROOT_DIR=`pwd`
cd $ROOT_DIR/EbfEmployees
mvn package
cd $ROOT_DIR/EbfAuthServer
mvn package
cd $ROOT_DIR
docker-compose up
