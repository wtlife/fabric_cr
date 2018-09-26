channel_create() {
    orderer_address=$1
    channel_name=$2
    org_msp=$3
    org_name=$4
    org_path=$5

    orderer_name=$6
    orderer_domain=$7

    generate_channeltx OneOrgChannel $channel_name $org_msp $org_path

    docker exec ${org_name}cli scripts/01_createChannel.sh $orderer_address $channel_name $orderer_name $orderer_domain
    if [ $? -ne 0 ]; then 
      echo "ERROR!!!创建${channel_name}失败!"
      exit 1
    fi 
}


channel_join() {
    orderer_address=$1
    peer_address=$2
    channel_name=$3
    org_name=$4

    orderer_name=$5
    orderer_domain=$6

    docker exec ${org_name}cli scripts/02_joinChannel.sh $orderer_address $peer_address $channel_name $orderer_name $orderer_domain
    if [ $? -ne 0 ]; then 
      echo "ERROR!!!${org_name}申请加入${channle_name}失败!"
      exit 1
    fi
}


channel_add_org() {
    channel_name=$1
    orderer_address=$2
    admin_org_name=$3
    applicant_org_msp=$4
    applicant_org_name=$5

    orderer_name=$6
    orderer_domain=$7

    docker exec ${admin_org_name}cli scripts/03_updateChannel_addOrg.sh $channel_name $orderer_address $applicant_org_msp $applicant_org_name $orderer_name $orderer_domain
    if [ $? -ne 0 ]; then 
      echo "ERROR!!!${channel_name}添加${applicant_org_name}失败!"
      exit 1
    fi
}


channel_remove_org() {
    channel_name=$1
    orderer_address=$2
    applicant_org_msp=$3
    admin_org_name=$4

    orderer_name=$5
    orderer_domain=$6

    docker exec ${admin_org_name}cli scripts/04_updateChannel_removeOrg.sh $channel_name $orderer_address $applicant_org_msp $orderer_name $orderer_domain
    if [ $? -ne 0 ]; then
      echo "ERROR!${channel_name}删除${applicant_org_msp}失败!"
      exit 1
    fi
}


generate_channeltx() {
    profile_name=$1
    channel_name=$2
    org_msg=$3
    org_path=$4

    _old_path=$PWD
    cd $org_path

    if [ ! -d "channel-artifacts" ]; then
    mkdir channel-artifacts
    fi

    export FABRIC_CFG_PATH=$PWD

    configtxgen -profile $profile_name -outputCreateChannelTx ./channel-artifacts/channel.tx -channelID $channel_name

    # modify channel config policy to a dominating policy
    dominating_policy='{"type":1,"value":{"identities":[{"principal":{"msp_identifier":"'$org_msg'","role":"ADMIN"},"principal_classification":"ROLE"}],"rule":{"n_out_of":{"n":1,"rules":[{"signed_by":0}]}},"version":0}}'
    action=".payload.data.config_update.write_set.groups.Application.policies.Admins.policy=$dominating_policy"

    configtxlator proto_decode --type common.Envelope --input ./channel-artifacts/channel.tx --output ./channel-artifacts/channel_config.json

    rm ./channel-artifacts/channel.tx

    jq "$action" ./channel-artifacts/channel_config.json > ./channel-artifacts/channel_config_updated.json

    configtxlator proto_encode --type common.Envelope --input ./channel-artifacts/channel_config_updated.json --output ./channel-artifacts/channel.tx
    cd $_old_path
    if [ $? -ne 0 ]; then
      echo "ERROR!创建channelTX文件失败!"
      exit 1
    fi
}
