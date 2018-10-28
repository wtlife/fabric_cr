# 基于Fabric的版权系统
|env|version|
|:----------|:----------|
|ubuntu|18.04|
|docker|18.03.0-ce|
|mysql|8.0.13|
|nodejs|v8.11.1|
|golang|1.10.2|
|java|10.0.2|
|spring-boot|2.0.6-REALEASE|
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

- 提供了登录注册功能，并做了持久化
- 提供，文件上传计算hash,使用sha1
- 提供版权登记，版权查询的入口