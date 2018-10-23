#!/bin/bash

CHANNEL_NAME=$1
CHAINCODE_NAME=$2
ARGS=$3

orderer_name=$4
orderer_domain=$5

set -x

# peer chaincode invoke --tls --cafile /opt/gopath/src/github.com/hyperledger/fabric/peer/crypto/ordererOrganizations/${orderer_domain}/orderers/${orderer_name}.${orderer_domain}/msp/tlscacerts/tlsca.${orderer_domain}-cert.pem  -C $CHANNEL_NAME -n $CHAINCODE_NAME -c '{"Args":'${ARGS}'}'
peer chaincode invoke -C $CHANNEL_NAME -n $CHAINCODE_NAME -c '{"Args":'${ARGS}'}'

set +x

exit 0

