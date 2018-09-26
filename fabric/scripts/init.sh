#!/bin/bash
generate_adv_artifacts(){
    consortium=$1
    orderer_domain=$2
    orderer_msp=$3
    orderer_name=$4
    org_domain=$5
    org_msp=$6
    org_name=$7
    declare -i org_peer_count=$8

    org_peer_outer_7051=$9
    org_peer_outer_7053=${10}
    couchdb_port=${11}
    org_path=${12}
    orderer_port=${13}

    mkdir -p $org_path

    generate_adv_fabric_yaml $consortium $orderer_domain $orderer_msp $orderer_name $org_domain $org_msp $org_name $org_peer_count $org_path
    generate_crypto $org_path
    generate_orderer_genesis OneOrgOrdererGenesis $org_path
    generate_org_info $org_msp $org_name $org_path
    generate_orderer_yaml $orderer_domain $orderer_msp $orderer_name $org_path $orderer_port
    generate_cli_yaml $org_domain $org_msp $org_name $org_path
    generate_peers_yaml $org_domain $org_msp $org_peer_count $org_peer_outer_7051 $org_peer_outer_7053 $couchdb_port $org_path
    # generate .env
    echo "COMPOSE_PROJECT_NAME=net" > $org_path/.env
    echo "CORE_LOGGING_GRPC=DEBUG" >> $org_path/.env
    if [ $? -ne 0 ]; then
      echo "ERROR !!!! generate${org_name}artifacts失败!!!"
      exit 1
    fi
}

generate_affi_artifacts(){
    org_domain=$1
    org_msp=$2
    org_name=$3
    declare -i org_peer_count=$4
    org_peer_outer_7051=$5
    org_peer_outer_7053=$6
    couchdb_port=$7
    org_path=$8

    mkdir -p $org_path

    generate_affi_fabric_yaml $org_domain $org_msp $org_name $org_peer_count $org_path
    generate_crypto $org_path
    generate_org_info $org_msp $org_name $org_path
    generate_cli_yaml $org_domain $org_msp $org_name $org_path
    generate_peers_yaml $org_domain $org_msp $org_peer_count $org_peer_outer_7051 $org_peer_outer_7053 $couchdb_port $org_path

    # generate .env
    echo "COMPOSE_PROJECT_NAME=net" > $org_path/.env
    echo "CORE_LOGGING_GRPC=DEBUG" >> $org_path/.env
    if [ $? -ne 0 ]; then
      echo "ERROR !!!! generate${org_name}artifacts失败!!!"
      exit 1
    fi
}


generate_adv_fabric_yaml(){
    consortium=$1
    orderer_domain=$2
    orderer_msp=$3
    orderer_name=$4
    org_domain=$5
    org_msp=$6
    org_name=$7
    org_peer_count=$8
    org_path=$9

    _old_path=$PWD
    ./template/adv_configtx.yaml.sh $consortium $orderer_domain $orderer_msp $orderer_name $org_domain $org_msp $org_name > $org_path/configtx.yaml
    ./template/adv_crypto-config.yaml.sh $orderer_domain $orderer_name $org_domain $org_peer_count > $org_path/crypto-config.yaml
    if [ $? -ne 0 ]; then
      echo "ERROR !!!! generate${org_name}yaml失败!!!"
      exit 1
    fi
    cd $_old_path
}

generate_affi_fabric_yaml(){
    org_domain=$1
    org_msp=$2 
    org_name=$3 
    org_peer_count=$4
    org_path=$5

    _old_path=$PWD
    ./template/affi_configtx.yaml.sh $org_domain $org_msp $org_name > $org_path/configtx.yaml
    ./template/affi_crypto-config.yaml.sh $org_domain $org_peer_count > $org_path/crypto-config.yaml
    if [ $? -ne 0 ]; then
      echo "ERROR !!!! generate${org_name}yaml失败!!!"
      exit 1
    fi
    cd $_old_path
}

generate_orderer_yaml(){
    orderer_domain=$1
    orderer_msp=$2
    orderer_name=$3
    org_path=$4
    orderer_port=$5

    _old_path=$PWD
    python3 ./template/docker-compose-generator.py -t 'orderer' -d $orderer_domain -m $orderer_msp -n $orderer_name --port-7050 $orderer_port -o $org_path/docker-compose-order.yaml
    cd $_old_path
}

generate_peers_yaml(){
    org_domain=$1
    org_msp=$2
    org_peer_count=$3
    org_peer_outer_7051=$4
    org_peer_outer_7053=$5
    couchdb_port=$6
    org_path=$7

    for ((index=0;index<$org_peer_count;index++))
    do
        generate_peer_yaml $org_domain $org_msp $index $org_peer_outer_7051 $org_peer_outer_7053 $couchdb_port $org_path 
        let "org_peer_outer_7051+=1000"
        let "org_peer_outer_7053+=1000"
        let "couchdb_port+=1000"
    done 
}


generate_peer_yaml(){
    org_domain=$1
    org_msp=$2
    org_peer_index=$3
    org_peer_outer_7051=$4
    org_peer_outer_7053=$5
    couchdb_port=$6
    org_path=$7

    _old_path=$PWD
    python3 ./template/docker-compose-generator.py -t 'peer' -d $org_domain -m $org_msp -n $org_name -i $org_peer_index --port-7051 $org_peer_outer_7051 --port-7053 $org_peer_outer_7053 --port-couchdb $couchdb_port -o $org_path/docker-compose-peer$org_peer_index.yaml
    cd $_old_path
}

generate_cli_yaml(){
    org_domain=$1
    org_msp=$2
    org_name=$3
    org_path=$4

    _old_path=$PWD
    python3 ./template/docker-compose-generator.py -t 'cli' -d $org_domain -m $org_msp -n $org_name -o $org_path/docker-compose-cli.yaml
    cd $_old_path
}

generate_org_info(){
    org_msp=$1
    org_name=$2
    org_path=$3

    _old_path=$PWD
    cd $org_path
    if [ ! -d "channel-artifacts" ]; then
        mkdir channel-artifacts
    fi
    export FABRIC_CFG_PATH=$PWD 

    configtxgen -printOrg $org_name > ./channel-artifacts/$org_name.json
    if [ $? -ne 0 ]; then
      echo "ERROR !!!! 创建${org_name}info失败!!!"
      exit 1
    fi
    cd $_old_path
}

generate_orderer_genesis(){
    profile_name=$1
    org_path=$2

    _old_path=$PWD
    cd $org_path
    if [ ! -d "channel-artifacts" ]; then
        mkdir channel-artifacts
    fi
    export FABRIC_CFG_PATH=$PWD

    configtxgen -profile $profile_name -outputBlock ./channel-artifacts/genesis.block
    if [ $? -ne 0 ]; then
      echo "ERROR !!!! 创建orderer_genesis失败!!!"
      exit 1
    fi
    cd $_old_path
}

generate_crypto(){
    org_path=$1

    _old_path=$PWD
    cd $org_path
    cryptogen generate --config=./crypto-config.yaml
    if [ $? -ne 0 ]; then
      echo "ERROR !!!! generate_crypto失败!!!"
      exit 1
    fi
    cd $_old_path
}
