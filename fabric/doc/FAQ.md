# 默认端口
- orderer: 7050
- peer: 7051


# chaincode命令参考
```bash
peer chaincode install -n mycc -v 1.0 -p github.com/chaincode/sacc

peer chaincode instantiate -o orderer.example1.com:7050 -C mychannel -n mycc -v 1.0 -c '{"Args":["a","10"]}' -P "OR ('Org1MSP1.peer')"

peer chaincode invoke -n mycc -c '{"Args":["set", "a", "20"]}' -C mychannel

peer chaincode query -n mycc -c '{"Args":["query","a"]}' -C mychannel

peer chaincode install -n mycc -v 2.0 -p github.com/chaincode/sacc
peer chaincode upgrade -o orderer.example1.com:7050 -C mychannel -n mycc -v 2.0 -c '{"Args":["a","11"]}' -P "OR ('Org1MSP1.peer','Org2MSP1.peer')"
```