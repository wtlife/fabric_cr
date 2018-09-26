
# peer
PEER_COUNT=2


# path
work_path=$PWD
root_path=$(cd ../../; pwd)
workspaces_path=$root_path/workspaces
 
source $root_path/scripts/container.sh
source $root_path/scripts/_utils.sh

source $work_path/scripts/channel.sh

deploy_adv(){
    cd $root_path

    adv_name=$1
    adv_domain=$2
    digest_domain=$3
    adv_msp=${adv_name}MSP
    adv_path=${workspaces_path}/${adv_name}

    # start container
    container_start_orderer $adv_path 
    container_start_cli $adv_path
    container_start_peers $adv_path 0 $[$PEER_COUNT-1]
    if [ $? -eq 0 ];then 
      echo "启动${adv_name}_container成功" 
    fi
    _wait_seconds
    
    # join digest channel
    join_digest_channel $adv_name $adv_domain
    if [ $? -eq 0 ];then 
      echo "广告主${adv_name}加入digest channel成功"
    fi
    cd $work_path 
}

deploy_affi(){
    cd $root_path
    affi_name=$1
    affi_domain=$2
    affi_msp=${affi_name}MSP
    affi_path=${workspaces_path}/${affi_name}

    # start container
    container_start_cli $affi_path
    container_start_peers $affi_path 0 $[$PEER_COUNT-1]
    if [ $? -eq 0 ];then 
      echo "启动${affi_name}container成功"
    fi
    _wait_seconds

    # join digest channel
    join_digest_channel $affi_name $affi_domain 
    if [ $? -eq 0 ];then 
      echo "广告平台${affi_name}加入摘要链${digest_name}成功"
    fi
    cd $work_path 
}

join_digest_channel(){
    admin_org_name="digest"
    admin_org_domain="digest.com"
    channel_name="digestchannel"
    chaincode_path="digestcc"

    applicant_org_name=$1
    applicant_org_domain=$2
    
    add_org $admin_org_name $admin_org_domain $channel_name $applicant_org_name $applicant_org_domain $chaincode_path
    if [ $? -ne 0 ];then 
      echo "ERROR!!!广告平台${affi_name}加入摘要链${digest_name}失败"
      exit 1
    fi
}

deploy_digest(){
    cd $root_path

    digest_name="digest"
    digest_domain="digest.com"

    digest_channel_name="digestchannel"
    digest_path=${workspaces_path}/${digest_name}
    
    #startcontainer
    container_start_orderer $digest_path
    container_start_cli $digest_path
    container_start_peers $digest_path 0 $[$PEER_COUNT-1]
    if [ $? -eq 0 ];then 
      echo "启动${digest_name}摘要链成功"
    fi
    _wait_seconds
    
    # #create digest channel
    create ${digest_name} ${digest_channel_name} ${digest_domain} "digestcc"
    if [ $? -eq 0 ];then 
      echo "创建${digest_name}摘要channel成功"
    fi   

    # echo "创建${digest_name}channel成功"
    cd $work_path 
}
