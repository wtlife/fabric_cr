## 1022
*RightUser.java*  
1. 在实现User接口的时候，不使用CA模块，应该实现相应的EnrollMent方法，加载证书，私钥匙；  
2. 在加载私钥的时候"EC",而不是"ECDSA"

## 1023
1. *报错First received frame was not SETTINGS. Hex dump for first 5 bytes: 1503010002*  
是由于在fabric端开启了TLS，而客户端未开启的缘故，处理方法：  
    关闭fabric端的tls，修改相应脚本.  
2. 重构了RightUser=> RightUser & RightUserEnrollment
3. 改为在链码中设置时间戳， regist函数增加返回值Txid

## 1024
1. 添加了spring-boot支持
2. 添加了用户注册模块

## 1025
1. 调试本地web服务
2. 安装mysql，坑
```
## 卸载mysql
sudo apt-get autoremove --purge mysql-server
sudo apt-get remove mysql-server
sudo apt-get autoremove mysql-server
sudo apt-get remove mysql-common 
dpkg -l |grep ^rc|awk '{print $2}' |sudo xargs dpkg -P

## ubuntu18.04 直接使用apt install ，安装的是5.7，这个版本是不支持18.04的，只有mysql8支持
sudo dpkg -i mysql-apt-config_0.8.6-1_all.deb
tab 选ok
sudo apt-get update
sudo apt-get install mysql-server 
```
3. 调通登录注册功能，注册还有些小问题
4. 修改代码结构

## 1026
1. 调通注册登录模块，增加用户主页
2. 实现文件上传，返回文件hash功能

## 1028
1. 调通了fabric与本地web服务
2. 发现sdk 注册的一个bug

## 1029
```
peer chaincode invoke -C mychannel -n myrightcc -c '{"Args":["regist","workk","wutao","center","0","0xhash","sigsigsig"]}'
df6c52802b8474cdc9ecf074ab8a26a5cd7288b6874be578ee87aeaef2487f73
peer chaincode query -C mychannel -n myrightcc -c '{"Args":["queryRightByName","work1","wutao","center"]}'

原来少写一行代码 哭~
channel.sendTransaction(resps);
```