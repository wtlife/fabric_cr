# Root
## 主命令
- cleanAll.sh 清除所有中间文件
- debug_main.sh 一键演示动态添加org
## 子命令

# order&org1 
模拟广告主和order节点
## 主命令

- debug_step1.sh 生成配置文件，创建order和org1，创建一个只有org1的channel并加入。
- debug_step2.sh
- debug_step3.sh

## 子命令
- 00_generateyaml.sh

| 参数名字       | 解释           | 示例  |
| :------------- |:-------------| :-----|
| CONSORTIUM     | 联盟名字 | SampleConsortium |
| OREDER_DOMAIN     | order的域名      |  example.com |
| OREDER_MSP     | order的msp Id      |  OrdererMSP |
| ORG_DOMAIN | organization的域名      |    org1.example.com |
| ORG_MSP | organization的msp Id     |    Org1MSP |


## docker container里的命令

.....
# org2
模拟广告平台

....