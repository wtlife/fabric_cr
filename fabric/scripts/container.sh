
container_start_orderer(){
    org_path=$1
    _old_path=$PWD

    cd $org_path
    docker-compose -f docker-compose-order.yaml up -d
    
    if [ $? -ne 0 ]; then
      echo "ERROR !!!!启动container失败!!!"
      exit 1
    fi
    
    cd $_old_path
}

container_start_cli(){
    org_path=$1
    _old_path=$PWD

    cd $org_path
    docker-compose -f docker-compose-cli.yaml up -d
    if [ $? -ne 0 ]; then
      echo "ERROR !!!! 启动cli失败!!!"
      exit 1
    fi
    cd $_old_path
}

container_start_peer(){
    org_path=$1
    peer_index=$2
    _old_path=$PWD

    cd $org_path
    docker-compose -f docker-compose-peer${peer_index}.yaml up -d

    if [ $? -ne 0 ]; then
      echo "ERROR !!!! 启动peer失败!!!"
      exit 1
    fi

    cd $_old_path
}

container_start_peers(){
    org_path=$1
    peer_start_index=$2
    peer_end_index=$3
    _old_path=$PWD

    cd $org_path
    for ((index=${peer_start_index};index<=${peer_end_index};index++));
    do
        docker-compose -f docker-compose-peer${index}.yaml up -d 
    done
    
    if [ $? -ne 0 ]; then
      echo "ERROR !!!! 启动peer失败!!!"
      exit 1
    fi
    cd $_old_path
}