#!/bin/bash

root_path=$(cd `dirname $0`; pwd)
cd $root_path
source ./scripts/_utils.sh

uplogd "$0"

docker rm -f $(docker ps -a | grep "peer\|couchdb\|.*cli\|orderer" | awk '{print $1}')
docker rmi -f $(docker images | grep "dev\|none\|test-vp\|-peer[0-9]"  | awk '{print $3}')

docker volume prune -f
docker network prune -f

# rm ../copyright/abe_temp/**

exit 0
