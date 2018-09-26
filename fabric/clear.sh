#!/bin/bash
root_path=$(cd `dirname $0`; pwd)
cd $root_path
source ./scripts/_utils.sh

uplogd "$0"

uplog "clean work spaces"
workspaces_path="$root_path/workspaces"

if [ "$workspaces_path" == "" ] || [ "$workspaces_path" == "/" ]; then
    uploge "Illegal root path: $workspaces_path"
    exit 1
else
    rm -rf $workspaces_path/**
fi

uplog "tear down "

docker rm -f $(docker ps -a | grep "peer\|couchdb\|.*cli\|orderer" | awk '{print $1}')
docker rmi -f $(docker images | grep "dev\|none\|test-vp\|-peer[0-9]"  | awk '{print $3}')

docker volume prune -f
docker network prune -f

exit 0
