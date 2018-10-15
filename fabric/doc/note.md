## 1015 链码开发
### 链码测试
1. start the network  
Terminal 1
```
docker-compose -f docker-compose-simple.yaml up
```

2. Build & start the chaincode
Terminal 2
```
docker exec -it chaincode bash

cd rightcc
go build

CORE_PEER_ADDRESS=peer:7052 CORE_CHAINCODE_ID_NAME=rightcc:0 ./rightcc
```

3. Use the chainCode 
Terminal 3
```
docker exec -it cli bash

peer chaincode install -p chaincodedev/chaincode/rightcc -n rightcc -v 0
peer chaincode instantiate -n rightcc -v 0 -c '{"Args":'[]'}' -C myc

peer chaincode invoke -n rightcc -c '{"Args":["regist","work","wutao","press","1948","0xhash","sigsigsig"]}' -C myc 
 
peer chaincode query -n rightcc -c '{"Args":["queryRightByName","work","wutao","press"]}' -C myc
```