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