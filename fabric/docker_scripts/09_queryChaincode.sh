#!/bin/bash

CHANNEL_NAME=$1
CHAINCODE_NAME=$2
ARGS=$3

set -x

peer chaincode query -C ${CHANNEL_NAME} -n $CHAINCODE_NAME -c '{"Args":'${ARGS}'}'

set +x 

exit 0

