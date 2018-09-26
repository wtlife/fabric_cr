chaincode_install(){
    chaincode_name=$1
    chaincode_version=$2
    chaincode_path=$3
    peer_address=$4
    admin_org_name=$5

    orderer_name=$6
    orderer_domain=$7

    docker exec ${admin_org_name}cli scripts/05_installChaincode.sh $chaincode_name $chaincode_version $chaincode_path $peer_address $orderer_name $orderer_domain
        
    if [ $? -ne 0 ]; then
      echo "${org_name}安装链码失败！"
      exit 1
    fi
}

chaincode_instantiate(){
    orderer_address=$1
    channel_name=$2
    chaincode_name=$3
    chaincode_version=$4
    instantiate_args=$5
    policy=$6
    admin_org_name=$7

    orderer_name=$8
    orderer_domain=$9
    
    docker exec ${admin_org_name}cli scripts/06_instantiateChaincode.sh $orderer_address $channel_name $chaincode_name $chaincode_version $instantiate_args "$policy" "$orderer_name" "$orderer_domain"

    if [ $? -ne 0 ]; then 
      echo "ERROR!!!实例化链码失败"
      exit 1
    fi
}

chaincode_invoke(){
    channel_name=$1
    chaincode_name=$2
    args=$3
    admin_org_name=$4

    orderer_name=$5
    orderer_domain=$6

    docker exec ${admin_org_name}cli scripts/08_invokeChaincode.sh $channel_name $chaincode_name $args $orderer_name $orderer_domain
    if [ $? -ne 0 ]; then 
      echo "ERROR!!!Invoke Failed!"
      exit 1
    fi
}

chaincode_query(){
    channel_name=$1
    chaincode_name=$2
    args=$3
    admin_org_name=$4

    docker exec ${admin_org_name}cli scripts/09_queryChaincode.sh $channel_name $chaincode_name $args
    if [ $? -ne 0 ]; then 
      echo "ERROR!!!Query Failed!"
      exit 1
    fi
}

chaincode_upgrade(){
    orderer_address=$1
    channel_name=$2
    chaincode_name=$3
    chaincode_version=$4
    upgrade_args=$5
    policy=$6
    admin_org_name=$7

    orderer_name=$8
    orderer_domain=$9

    docker exec ${admin_org_name}cli scripts/07_upgradeChaincode.sh $orderer_address $channel_name $chaincode_name $chaincode_version "$upgrade_args" "$policy" $orderer_name $orderer_domain
    if [ $? -ne 0 ]; then 
      echo "ERROR!!!链码升级失败!"
      exit 1
    fi
}