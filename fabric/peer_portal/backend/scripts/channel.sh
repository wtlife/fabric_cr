CHAINCODE_EMPTY_ARGS='[]'

# path
work_path=$PWD
root_path=$(cd ../../; pwd)
workspaces_path=$root_path/workspaces

# import
source $root_path/scripts/_utils.sh
source $root_path/scripts/channel.sh
source $root_path/scripts/chaincode.sh

CHAINCODE_EMPTY_ARGS='[]'
PEER_COUNT=2

work_path=$PWD
root_path=$(cd ../../; pwd)
workspaces_path=$root_path/workspaces

_wait_seconds () {
    delay=$1
    delay=${delay:-"3"}
    sleep $delay
}

create(){
    org_name=$1
    channel_name=$2
    org_domain=$3
    chaincode_path=$4

    org_msp=${org_name}MSP
    org_path=${workspaces_path}/${org_name}

    orderer_domain=${org_domain}
    orderer_name=${org_name}orderer
    orderer_address=${orderer_name}.${orderer_domain}:7050

    chaincode_name="${channel_name}${chaincode_path}"
    chaincode_version='1.0'
    chaincode_version_policy="OR ('"$org_msp".peer')" 


    #create channel
    channel_create ${orderer_address} ${channel_name} ${org_msp} ${org_name} ${org_path}
    _wait_seconds

    peers_join_channel_install_chaincode $orderer_address $channel_name $org_name $org_domain $chaincode_version $chaincode_path
    _wait_seconds
    # instantiate
    chaincode_instantiate $orderer_address $channel_name $chaincode_name $chaincode_version $CHAINCODE_EMPTY_ARGS "$chaincode_version_policy" $org_name
    if [ $? -eq 0 ];then 
      echo "创建${channel_name}"成功
    fi
}

add_org(){
    admin_org_name=$1
    admin_org_domain=$2
    channel_name=$3
    applicant_org_name=$4
    applicant_org_domain=$5
    chaincode_path=$6

    orderer_domain=${admin_org_domain}
    orderer_name=${admin_org_name}orderer
    orderer_address=${orderer_name}.${orderer_domain}:7050

    chaincode_name="${channel_name}${chaincode_path}"
    chaincode_version='1.0'

    cp $workspaces_path/$applicant_org_name/channel-artifacts/$applicant_org_name.json $workspaces_path/$admin_org_name/channel-artifacts/$applicant_org_name.json
    echo "拷贝 $applicant_org_name.json 完成"
    # modify channel
    channel_add_org $channel_name $orderer_address $admin_org_name ${applicant_org_name}MSP $applicant_org_name
    echo "更改channel配置成功"
    _wait_seconds

    peers_join_channel_install_chaincode $orderer_address $channel_name $applicant_org_name $applicant_org_domain $chaincode_version $chaincode_path
    if [ $? -eq 0 ];then 
      echo "在应用${channel_name}中添加${applicant_org_name}成功"
    fi
}

peers_join_channel_install_chaincode(){
    orderer_address=$1
    channel_name=$2
    org_name=$3
    org_domain=$4
    chaincode_version=$5
    chaincode_path=$6   
    
    for ((index=0;index<${PEER_COUNT};index++));
    do
    org_peer_address="peer${index}.${org_domain}:7051"
    channel_join $orderer_address $org_peer_address $channel_name $org_name
    _wait_seconds

    chaincode_install $chaincode_name $chaincode_version $chaincode_path $org_peer_address $org_name
    done 

    if [ $? -eq 0 ];then 
      echo "${org_name}申请加入${channel_name}成功"
    fi
}

remove_org(){
    admin_org_name=$1
    admin_org_domain=$2
    channel_name=$3
    applicant_org_name=$4
    chaincode_path=$5

    orderer_domain=${admin_org_domain}
    orderer_name=${admin_org_name}_orderer
    orderer_address=${orderer_name}.${orderer_domain}:7050

    channel_remove_org $channel_name $orderer_address ${applicant_org_name}MSP $admin_org_name
    
    if [ $? -eq 0 ]; then
    echo "${channel_name}中删除${applicant_org_name}成功"
    fi
}