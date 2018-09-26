#!/bin/bash

ORDERER_ADDRESS=$1
CHANEL_NAME=$2

orderer_name=$3
orderer_domain=$4


set -x
echo 
peer channel create -o $ORDERER_ADDRESS -c $CHANEL_NAME -f ./channel-artifacts/channel.tx --tls $CORE_PEER_TLS_ENABLED --cafile /opt/gopath/src/github.com/hyperledger/fabric/peer/crypto/ordererOrganizations/${orderer_domain}/orderers/${orderer_name}.${orderer_domain}/msp/tlscacerts/tlsca.${orderer_domain}-cert.pem  
set +x

exit 0
