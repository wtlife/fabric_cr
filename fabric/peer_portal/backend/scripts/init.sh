#Consortium
CONSORTIUM=SampleConsortium
PEER_COUNT=2

# path
work_path=$PWD
root_path=$(cd ../../; pwd)
workspaces_path=$root_path/workspaces
# echo "$work_path"
# echo "$root_path"

source $root_path/scripts/init.sh

export FABRIC_LOCAL_CTNR='YES'

init_adv(){
    cd $root_path
    adv_name=$1
    adv_domain=$2
    adv_peer_outer_7051=$3
    couchDB_port=$4
    orderer_port=$5

    adv_peer_outer_7053=$[$adv_peer_outer_7051+2]
    adv_msp=${adv_name}MSP
    adv_path=${workspaces_path}/${adv_name}

    orderer_domain=${adv_domain}
    orderer_name=${adv_name}orderer
    orderer_msp=${orderer_name}MSP 
    
    mkdir -p $adv_path
    generate_adv_artifacts ${CONSORTIUM}  $orderer_domain $orderer_msp $orderer_name $adv_domain $adv_msp $adv_name $PEER_COUNT $adv_peer_outer_7051 $adv_peer_outer_7053 $couchDB_port $adv_path $orderer_port
    if [ $? -eq 0 ];then 
      echo "初始化广告主${adv_name}成功"
    fi   
    cd $work_path
}

init_affi(){
    cd $root_path
    
    affi_name=$1
    affi_domain=$2
    affi_peer_outer_7051=$3
    couchDB_port=$4

    affi_peer_outer_7053=$[$affi_peer_outer_7051+2]
    affi_msp=${affi_name}MSP
    affi_path=${workspaces_path}/${affi_name}
    mkdir -p $affi_path
    
    generate_affi_artifacts $affi_domain $affi_msp $affi_name $PEER_COUNT $affi_peer_outer_7051 $affi_peer_outer_7053 $couchDB_port $affi_path

    if [ $? -eq 0 ];then 
      echo "初始化广告平台${affi_name}成功"
    fi 
    cd $work_path
}

init_digest(){
    cd $root_path
    
    #bypass passin paramter and use fiedname
    digest_name="digest"
    digest_domain="digest.com"
    digest_peer_outer_7051=$3
    couchDB_port=$4
    orderer_port=$5

    digest_peer_outer_7053=$[$digest_peer_outer_7051+2]
    digest_msp=${digest_name}MSP
    digest_path=${workspaces_path}/${digest_name}

    orderer_domain=${digest_domain}
    orderer_name=${digest_name}orderer
    orderer_msp=${orderer_name}MSP 
    
    mkdir -p $digest_path
    generate_adv_artifacts ${CONSORTIUM} $orderer_domain $orderer_msp $orderer_name $digest_domain $digest_msp $digest_name $PEER_COUNT $digest_peer_outer_7051 $digest_peer_outer_7053 $couchDB_port $digest_path $orderer_port
    if [ $? -eq 0 ];then 
      echo "初始化Digest摘要链${digest_name}成功"
    fi
    cd $work_path
    
}
