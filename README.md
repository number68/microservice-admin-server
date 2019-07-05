microservice-admin-server
================================

### Building this project
mvn clean install 

### Access
http://localhost:8080/

![](https://github.com/number68/microservice-admin-server/blob/master/images/example.jpg)

- 在application.yml修改eureka用户名密码
- 在application.yml修改javamelody的存储路径配置storage-directory
- 如果被监控服务httptrace有crash的问题，请参照本工程的WebConfig类做改造