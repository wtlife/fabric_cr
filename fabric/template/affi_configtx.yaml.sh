#!/bin/bash
ORG_DOMAIN="$1"
ORG_MSP="$2"
ORG_NAME=$3

echo "---"
echo "Organizations:"
echo "    - &Org2"
echo "        Name: ${ORG_NAME}"
echo "        ID: ${ORG_MSP}"
echo "        MSPDir: crypto-config/peerOrganizations/${ORG_DOMAIN}/msp"
echo "        AnchorPeers:"
echo "            - Host: peer0.${ORG_DOMAIN}"
echo "              Port: 7051"
echo ""
exit 0