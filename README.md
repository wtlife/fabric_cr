# 基于Fabric的版权系统

## Network Structure

- orderer copyright.com

- org1 center(版权中心)  
  - peer0
        peer0.center.copyright.com
  - peer1

- org2 press1(出版社1) 
  - peer0
  - peer1

## Chaincode api
ChainCodeName="myrightcc"  

1. invoke  

| Name | Input | Return|
|:----------|:----------|:----------|
|regist|Right right|null|

2. query    

| Name | Input | Return|
|:----------|:----------|:----------|
|queryRightByName|String name,String author,String press|Right right|
