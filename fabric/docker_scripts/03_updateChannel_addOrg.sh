#!/bin/bash

CHANNEL_NAME=$1
ORDERER_ADDRESS=$2

ORG2_MSP=$3
ORG2_NAME=$4

orderer_name=$5
orderer_domain=$6

ADD_ORG_ENVELOPE_FILE_NAME='add_org_evelope'

./scripts/00_install_jq.sh >/dev/null 2>&1
./scripts/updateChannel/01_clean.sh >/dev/null 2>&1

./scripts/updateChannel/02_fetch_channel.sh $CHANNEL_NAME $ORDERER_ADDRESS $orderer_name $orderer_domain
./scripts/updateChannel/03_add_org.sh $CHANNEL_NAME $ORG2_MSP $ORG2_NAME $ADD_ORG_ENVELOPE_FILE_NAME
./scripts/updateChannel/05_sign_update.sh $ADD_ORG_ENVELOPE_FILE_NAME $CHANNEL_NAME $ORDERER_ADDRESS $orderer_name $orderer_domain

exit 0
