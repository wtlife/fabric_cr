#!/bin/bash

ORDERER_ADDRESS=$1
PEER_ADDRESS=$2
CHANNEL_NAME=$3

orderer_name=$4
orderer_domain=$5

export CORE_PEER_ADDRESS=$PEER_ADDRESS

rm -f ${CHANNEL_NAME}.block

set -x

# peer channel fetch 0 ${CHANNEL_NAME}.block -o $ORDERER_ADDRESS -c $CHANNEL_NAME --tls --cafile /opt/gopath/src/github.com/hyperledger/fabric/peer/crypto/ordererOrganizations/${orderer_domain}/orderers/${orderer_name}.${orderer_domain}/msp/tlscacerts/tlsca.${orderer_domain}-cert.pem
peer channel fetch 0 ${CHANNEL_NAME}.block -o $ORDERER_ADDRESS -c $CHANNEL_NAME 
# peer channel join -b ${CHANNEL_NAME}.block --tls --cafile /opt/gopath/src/github.com/hyperledger/fabric/peer/crypto/ordererOrganizations/${orderer_domain}/orderers/${orderer_name}.${orderer_domain}/msp/tlscacerts/tlsca.${orderer_domain}-cert.pem
peer channel join -b ${CHANNEL_NAME}.block 

set +x

exit 0
