#!/bin/bash

CHAINCODE_NAME=$1
CHAINCODE_VERSION=$2
CHAINCODE_PATH=$3
PEER_ADDRESS=$4

export CORE_PEER_ADDRESS=${PEER_ADDRESS}

set -x

peer chaincode install -n ${CHAINCODE_NAME} -v ${CHAINCODE_VERSION} -p github.com/chaincode/$CHAINCODE_PATH --tls --cafile /opt/gopath/src/github.com/hyperledger/fabric/peer/crypto/ordererOrganizations/${orderer_domain}/orderers/${orderer_name}.${orderer_domain}/msp/tlscacerts/tlsca.${orderer_domain}-cert.pem

set +x

exit 0

