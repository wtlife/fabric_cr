## WorkFlow
```
<!-- start Fabric -->
./fabric/start_fabric.sh
<!-- start javaweb -->
run springBootApplication

<!-- stop&clean -->
stop application
./fabric/teardown.sh
./fabric/clear.sh
```


|STEP|function|input|return|description|
|:----------|:----------|:----------|:----------|:----------|
|1|registUser|username,passowrd|privateKey,publicKey|注册用户,生成密钥对,私钥要自己保存|
|2|fileUpload|file,privateKey|filePath,fileHash,fileSignature|文件上传,计算摘要,对摘要进行签名|
|3|registRight|fileName,author,press,fileHash,fileSignature|boolean,Txid,peerInfo|在区块链上登记版权|
|4|queryRight|fileName,author,press|name,author,press,time,hash,signature|在区块链上查询版权,返回版权相关信息|
|5|fileVerify|fileHash,fileSignature,publicKey|boolean|文件验证|
|6|queryTx|TxId|txId,validationCode|查询交易信息|

# 基于Fabric的版权系统
|env|version|
|:----------|:----------|
|ubuntu|18.04|
|docker|18.03.0-ce|
|mysql|8.0.13|
|nodejs|v8.11.1|
|golang|1.10.2|
|java|JDK8|
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
    ID        string `json:"id"`
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

- 提供了登录注册功能，用户名密码身份ID做了持久化
- 提供生成公私钥对的功能，私钥未作持久化
- 提供，文件上传计算摘要
- 提供了文件签名的功能
- 提供版权登记，版权查询的入口
- 提供文件验证功能
- 提供交易信息查询功能

|func|algorithm|
|:----------|:----------|
|fileHash|SHA-1|
|pri&pub|ECC|
|fileSignature|SHA-1withECDSA||


