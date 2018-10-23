#!/bin/bash

# param1 channel name
# param2 order address

CHANNEL_NAME=$1
ORDERER_ADDRESS=$2
CHANNEL_BLOCK=$1_block

orderer_name=$3
orderer_domain=$4

set -x
rm -f $CHANNEL_BLOCK.pb

# peer channel fetch config $CHANNEL_BLOCK.pb -o $ORDERER_ADDRESS -c $CHANNEL_NAME --tls --cafile /opt/gopath/src/github.com/hyperledger/fabric/peer/crypto/ordererOrganizations/${orderer_domain}/orderers/${orderer_name}.${orderer_domain}/msp/tlscacerts/tlsca.${orderer_domain}-cert.pem
peer channel fetch config $CHANNEL_BLOCK.pb -o $ORDERER_ADDRESS -c $CHANNEL_NAME 
set +x

exit 0 
