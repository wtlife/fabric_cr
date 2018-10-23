# 基于Fabric的版权系统
## Fabric

### Data
```
type Right struct {
	Name      string `json:"Name"`
	Author    string `json:"Author"`
	Press     string `json:"Press"`
	Timestamp int64  `json:"Ts"`
	Hash      string `json:"Hash"`
	Signature string `json:"Sig"`
}
```

### Network Structure

- orderer
    copyright.com

- org
    - center(版权中心)  
        - peer0  
            peer0.center.copyright.com
        - peer1  
            peer1.center.copyright.com

    - press1(出版社1) 
        - peer0
        - peer1

### Chaincode api
ChannelName="mychannel"
ChainCodeName="myrightcc"  

1. invoke  

| Name | Input | Return|
|:----------|:----------|:----------|
|regist|Right right|byte[] Txid|

2. query    

| Name | Input | Return|
|:----------|:----------|:----------|
|queryRightByName|String name,String author,String press|Right right|

## Client(本地服务)