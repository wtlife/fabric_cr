#!/bin/bash

CHANNEL_NAME=$1
ORDERER_ADDRESS=$2

ORG_MSP=$3

orderer_name=$4
orderer_domain=$5

REMOVE_ORG_ENVELOPE_FILE_NAME='remove_org_evelope'

./scripts/00_install_jq.sh >/dev/null 2>&1
./scripts/updateChannel/01_clean.sh
./scripts/updateChannel/02_fetch_channel.sh $CHANNEL_NAME $ORDERER_ADDRESS $orderer_name $orderer_domain
./scripts/updateChannel/04_remove_org.sh $CHANNEL_NAME $ORG_MSP $REMOVE_ORG_ENVELOPE_FILE_NAME
./scripts/updateChannel/05_sign_update.sh $REMOVE_ORG_ENVELOPE_FILE_NAME $CHANNEL_NAME $ORDERER_ADDRESS $orderer_name $orderer_domain

exit 0
