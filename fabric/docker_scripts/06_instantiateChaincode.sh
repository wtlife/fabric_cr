#!/bin/bash

ORDERER_ADDRESS=$1
CHANNEL_NAME=$2
CHAINCODE_NAME=$3
CHAINCODE_VERSION=$4
ARGS=$5
POLICY=$6

orderer_name=$7
orderer_domain=$8

set -x

peer chaincode instantiate -o ${ORDERER_ADDRESS} --tls --cafile /opt/gopath/src/github.com/hyperledger/fabric/peer/crypto/ordererOrganizations/${orderer_domain}/orderers/${orderer_name}.${orderer_domain}/msp/tlscacerts/tlsca.${orderer_domain}-cert.pem -C ${CHANNEL_NAME} -n ${CHAINCODE_NAME} -v ${CHAINCODE_VERSION} -c '{"Args":'${ARGS}'}' -P "$POLICY"

set +x

exit 0

