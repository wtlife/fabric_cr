#!/bin/bash
root_path=$(cd `dirname $0`; pwd)
cd $root_path

source scripts/_utils.sh
source scripts/channel.sh
source scripts/container.sh
source scripts/chaincode.sh
source scripts/init.sh

uplogd "$0"

export FABRIC_LOCAL_CTNR='YES'

workspaces_path="$root_path/workspaces"
ORG1_PATH="$workspaces_path/center"
ORG2_PATH="$workspaces_path/press1"

mkdir -p $ORG1_PATH
mkdir -p $ORG2_PATH

echo $workspaces_path
echo $ORG1_PATH
echo $ORG2_PATH

# Parameter:
CONSORTIUM=SamConsortium

#ORDERER
ORDERER_NAME=orderer
ORDERER_DOMAIN=copyright.com
ORDERER_MSP=copyrightMSP
orderer_port=7050
ORDERER_ADDRESS=${ORDERER_NAME}.$ORDERER_DOMAIN:${orderer_port}

#ORG1
ORG1_NAME=center
ORG1_DOMAIN=${ORG1_NAME}.${ORDERER_DOMAIN}
ORG1_MSP=${ORG1_NAME}MSP
ORG1_PEER_COUNT=2
ORG1_PEER_OUTER_PORT=11051
ORG1_PEER_OUTER_PORT2=11053
COUCHDB1_PORT=5984

#ORG2
ORG2_NAME=press1
ORG2_DOMAIN=$ORG2_NAME.$ORDERER_DOMAIN
ORG2_MSP=${ORG2_NAME}MSP
ORG2_PEER_COUNT=2
ORG2_PEER_OUTER_PORT=14052
ORG2_PEER_OUTER_PORT2=14054
COUCHDB2_PORT=5985

#CHANNEL
CHANNEL_NAME=mychannel

#CHAINCODE
CHAINCODE_VERSION_1="1.0"
CHAINCODE_VERSION_1_POLICY="OR ('"${ORG1_MSP}".peer')" 
CHAINCODE_VERSION_2="2.0"
CHAINCODE_VERSION_2_POLICY="OR ('"${ORG1_MSP}".peer','"${ORG2_MSP}".peer')" 

# right chaincode
CHAINCODE_PATH="rightcc"
CHAINCODE_NAME="myrightcc"
CHAINCODE_EMPTY_ARGS='[]'
CHAINCODE_QUERY_ARGS='["queryRightByName","work1","wutao","org1"]'
CHAINCODE_INVOKE_ARGS_1='["regist","work1","wutao","org1","1948","0xhash","sigsigsig"]' 
CHAINCODE_INVOKE_ARGS_2='["regist","work2","wutao","org1","1949","0xhash","sigsigsig"]' 
CHAINCODE_INVOKE_ARGS_3='["regist","work3","wutao","org2","1950","0xhash","sigsigsig"]' 

uplogd "clean"
if [ "$workspaces_path" == "" ] || [ "$workspaces_path" == "/" ]; then
    echo "Illegal root path: $workspaces_path"
    exit 1
else
    rm -rf $workspaces_path
fi

uplogd "shut down previous container"
./00_teardown.sh >/dev/null 2>&1

uplogd "clear old cert for advertiser and affiliate"
generate_adv_artifacts $CONSORTIUM $ORDERER_DOMAIN $ORDERER_MSP $ORDERER_NAME $ORG1_DOMAIN $ORG1_MSP $ORG1_NAME $ORG1_PEER_COUNT $ORG1_PEER_OUTER_PORT $ORG1_PEER_OUTER_PORT2 $COUCHDB1_PORT $ORG1_PATH $orderer_port

uplogd 'start container'

container_start_orderer $ORG1_PATH 
container_start_cli $ORG1_PATH 
container_start_peers $ORG1_PATH 0 $[$ORG1_PEER_COUNT-1]

channel_create $ORDERER_ADDRESS $CHANNEL_NAME $ORG1_MSP $ORG1_NAME $ORG1_PATH $ORDERER_NAME $ORDERER_DOMAIN

for ((index=0;index<${ORG1_PEER_COUNT};index++));
do
    ORG1_PEER_ADDRESS="peer${index}.${ORG1_DOMAIN}:7051"
    channel_join $ORDERER_ADDRESS $ORG1_PEER_ADDRESS $CHANNEL_NAME $ORG1_NAME $ORDERER_NAME $ORDERER_DOMAIN

    _wait_seconds

    ORG1_PEER_ADDRESS="peer${index}.${ORG1_DOMAIN}:7051"
    chaincode_install $CHAINCODE_NAME $CHAINCODE_VERSION_1 $CHAINCODE_PATH $ORG1_PEER_ADDRESS $ORG1_NAME $ORDERER_NAME $ORDERER_DOMAIN
done

chaincode_instantiate $ORDERER_ADDRESS $CHANNEL_NAME $CHAINCODE_NAME $CHAINCODE_VERSION_1 "$CHAINCODE_EMPTY_ARGS" "$CHAINCODE_VERSION_1_POLICY" "$ORG1_NAME" "$ORDERER_NAME" "$ORDERER_DOMAIN"
_wait_seconds
chaincode_invoke $CHANNEL_NAME $CHAINCODE_NAME $CHAINCODE_INVOKE_ARGS_1 $ORG1_NAME $ORDERER_NAME $ORDERER_DOMAIN
_wait_seconds

chaincode_query $CHANNEL_NAME $CHAINCODE_NAME $CHAINCODE_QUERY_ARGS $ORG1_NAME
_wait_seconds

echo
echo ">>>>>>>>>>>>>>>>>>>>>>  Org 2 step 1  <<<<<<<<<<<<<<<<<<<<<<<"
generate_affi_artifacts $ORG2_DOMAIN $ORG2_MSP $ORG2_NAME $ORG2_PEER_COUNT $ORG2_PEER_OUTER_PORT $ORG2_PEER_OUTER_PORT2 $COUCHDB2_PORT $ORG2_PATH
container_start_cli $ORG2_PATH 
container_start_peers $ORG2_PATH 0 $[$ORG2_PEER_COUNT-1]
_wait_seconds

echo
echo ">>>>>>>>>>>>>>>>>>>>>>  Copy org2.json & orderer_material  <<<<<<<<<<<<<<<<<<<<<<<"
cp $ORG2_PATH/channel-artifacts/$ORG2_NAME.json $ORG1_PATH/channel-artifacts/$ORG2_NAME.json
cp -R $ORG1_PATH/crypto-config/ordererOrganizations $ORG2_PATH/crypto-config/


echo
echo ">>>>>>>>>>>>>>>>>>>>>>  Org 1 step 2  <<<<<<<<<<<<<<<<<<<<<<<"
channel_add_org $CHANNEL_NAME $ORDERER_ADDRESS $ORG1_NAME $ORG2_MSP $ORG2_NAME $ORDERER_NAME $ORDERER_DOMAIN
_wait_seconds

for ((index=0;index<${ORG1_PEER_COUNT};index++));
do
    ORG1_PEER_ADDRESS="peer${index}.${ORG1_DOMAIN}:7051"
    chaincode_install $CHAINCODE_NAME $CHAINCODE_VERSION_2 $CHAINCODE_PATH $ORG1_PEER_ADDRESS $ORG1_NAME $ORDERER_NAME $ORDERER_DOMAIN
done

chaincode_upgrade $ORDERER_ADDRESS $CHANNEL_NAME $CHAINCODE_NAME $CHAINCODE_VERSION_2 "$CHAINCODE_EMPTY_ARGS" "$CHAINCODE_VERSION_2_POLICY" $ORG1_NAME $ORDERER_NAME $ORDERER_DOMAIN
_wait_seconds

chaincode_query $CHANNEL_NAME $CHAINCODE_NAME $CHAINCODE_QUERY_ARGS $ORG1_NAME
_wait_seconds

echo
echo ">>>>>>>>>>>>>>>>>>>>>>  Org 2 step 2  <<<<<<<<<<<<<<<<<<<<<<<"
for ((index=0;index<${ORG2_PEER_COUNT};index++));
do
    ORG2_PEER_ADDRESS="peer${index}.${ORG2_DOMAIN}:7051"
    channel_join $ORDERER_ADDRESS $ORG2_PEER_ADDRESS $CHANNEL_NAME $ORG2_NAME $ORDERER_NAME $ORDERER_DOMAIN
    _wait_seconds

    chaincode_install $CHAINCODE_NAME $CHAINCODE_VERSION_2 $CHAINCODE_PATH $ORG2_PEER_ADDRESS $ORG2_NAME $ORDERER_NAME $ORDERER_DOMAIN
done
_wait_seconds

chaincode_invoke $CHANNEL_NAME $CHAINCODE_NAME $CHAINCODE_INVOKE_ARGS_2 $ORG2_NAME  $ORDERER_NAME $ORDERER_DOMAIN
_wait_seconds

chaincode_query $CHANNEL_NAME $CHAINCODE_NAME $CHAINCODE_QUERY_ARGS $ORG2_NAME
_wait_seconds

# echo
# echo ">>>>>>>>>>>>>>>>>>>>>>  Org 1 step 3  <<<<<<<<<<<<<<<<<<<<<<<"
# channel_remove_org $CHANNEL_NAME $ORDERER_ADDRESS $ORG2_MSP $ORG1_NAME $ORDERER_NAME $ORDERER_DOMAIN
# _wait_seconds

# chaincode_invoke $CHANNEL_NAME $CHAINCODE_NAME $CHAINCODE_INVOKE_ARGS_3 $ORG1_NAME $ORDERER_NAME $ORDERER_DOMAIN
# _wait_seconds

# chaincode_query $CHANNEL_NAME $CHAINCODE_NAME $CHAINCODE_QUERY_ARGS $ORG1_NAME
# _wait_seconds

# echo
# echo ">>>>>>>>>>>>>>>>>>>>>>  Org 2 step 4  <<<<<<<<<<<<<<<<<<<<<<<"
# chaincode_query $CHANNEL_NAME $CHAINCODE_NAME $CHAINCODE_QUERY_ARGS $ORG2_NAME
# _wait_seconds

echo ">>>>>>>>>>>>>>>>>>>>>>  Done  <<<<<<<<<<<<<<<<<<<<<<<"
exit 0
