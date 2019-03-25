##  演示前要做的工作
1. 保证网络状态正常
2. 启动dcoker 服务  
```
sudo service docker start
```
3. 已生成公共参数与主密钥,且不被删除


## 注意事项
- fabric中的peer地址  
在展示时，应修改FabricService.java文件中的相关方法的peer地址
- 关于用户属性的处理  
为方便展示，用户属性直接写死在Config.java文件中，在展示时应修改UserService.java 的第一个方法
- 关于访问策略的处理  
从前端传字符串
见FabricService.java registRight方法  


## 切换用户查询信息时候
修改UserService中注册用户的attr字段
修改FabricService.java query方法中的attr字段


## 测试数据

用户名test03255注册成功！
你的publicKey:MFkwEwYHKoZIzj0CAQYIKoZIzj0DAQcDQgAEVj2yUhw8LZco87OkJzet2FYYqvTdbg6chkAl9QFpfz/YmLm+WamWqT1cgUhcPyrNdlPAnG9yYmlSRzzzA0AAYQ==
你的privateKey:MEECAQAwEwYHKoZIzj0CAQYIKoZIzj0DAQcEJzAlAgEBBCA8+JMzYU4ZN4p0boCsboi0buhpEUGhMSFEKSPdk9r51Q==
请妥善保管!无备份!

test03255
test
policy 4,4,2,2

test03259
attr 2,2

test11
attr 1,1

