#!/bin/bash 

CHANNEL_NAME=$1
ORG_MSP=$2
OUTPUT_FILENAME=$3

configtxlator proto_decode --input ${CHANNEL_NAME}_block.pb --type common.Block | jq .data.data[0].payload.data.config > tmp_remove_channel_config.json

MODIDFED_PLACE='del(.channel_group.groups.Application.groups.'${ORG_MSP}')'

jq ${MODIDFED_PLACE} tmp_remove_channel_config.json > tmp_remove_modified_channel_config.json

configtxlator proto_encode --input tmp_remove_channel_config.json --type common.Config --output tmp_remove_channel_config.pb

configtxlator proto_encode --input tmp_remove_modified_channel_config.json --type common.Config --output tmp_remove_modified_channel_config.pb

configtxlator compute_update --channel_id $CHANNEL_NAME --original tmp_remove_channel_config.pb --updated tmp_remove_modified_channel_config.pb --output tmp_remove_org_update.pb

configtxlator proto_decode --input tmp_remove_org_update.pb --type common.ConfigUpdate | jq . > tmp_remove_org_update.json

echo '{"payload":{"header":{"channel_header":{"channel_id":"'${CHANNEL_NAME}'", "type":2}},"data":{"config_update":'$(cat tmp_remove_org_update.json)'}}}' | jq . > tmp_remove_org_update_in_envelope.json

configtxlator proto_encode --input tmp_remove_org_update_in_envelope.json --type common.Envelope --output ${OUTPUT_FILENAME}.pb

exit 0
