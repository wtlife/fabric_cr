#!/bin/bash

SIGN_FILENAME=$1
CHANNEL_NAME=$2
ORDERER_ADDRESS=$3

orderer_name=$4
orderer_domain=$5

set -x
peer channel signconfigtx -f ${SIGN_FILENAME}.pb --tls --cafile /opt/gopath/src/github.com/hyperledger/fabric/peer/crypto/ordererOrganizations/${orderer_domain}/orderers/${orderer_name}.${orderer_domain}/msp/tlscacerts/tlsca.${orderer_domain}-cert.pem

peer channel update -f ${SIGN_FILENAME}.pb -c $CHANNEL_NAME -o ${ORDERER_ADDRESS} --tls --cafile /opt/gopath/src/github.com/hyperledger/fabric/peer/crypto/ordererOrganizations/${orderer_domain}/orderers/${orderer_name}.${orderer_domain}/msp/tlscacerts/tlsca.${orderer_domain}-cert.pem
set +x

exit 0
