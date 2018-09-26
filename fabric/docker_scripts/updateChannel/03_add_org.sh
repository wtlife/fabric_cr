#!/bin/bash

CHANNEL_NAME=$1
ORG_MSP=$2
ORG_NAME=$3
OUTPUT_FILENAME=$4

configtxlator proto_decode --input ${CHANNEL_NAME}_block.pb --type common.Block | jq .data.data[0].payload.data.config > tmp_add_channel_config.json

MODIDFED_PLACE='.[0] * {"channel_group":{"groups":{"Application":{"groups": {"'${ORG_MSP}'":.[1]}}}}}'

jq -s "${MODIDFED_PLACE}" tmp_add_channel_config.json ./channel-artifacts/${ORG_NAME}.json > tmp_add_modified_channel_config.json

configtxlator proto_encode --input tmp_add_channel_config.json --type common.Config --output tmp_add_channel_config.pb

configtxlator proto_encode --input tmp_add_modified_channel_config.json --type common.Config --output tmp_add_modified_channel_config.pb

configtxlator compute_update --channel_id $CHANNEL_NAME --original tmp_add_channel_config.pb --updated tmp_add_modified_channel_config.pb --output tmp_add_org_update.pb

configtxlator proto_decode --input tmp_add_org_update.pb --type common.ConfigUpdate | jq . > tmp_add_org_update.json

echo '{"payload":{"header":{"channel_header":{"channel_id":"'${CHANNEL_NAME}'", "type":2}},"data":{"config_update":'$(cat tmp_add_org_update.json)'}}}' | jq . > tmp_add_org_update_in_envelope.json

configtxlator proto_encode --input tmp_add_org_update_in_envelope.json --type common.Envelope --output ${OUTPUT_FILENAME}.pb

exit  0
