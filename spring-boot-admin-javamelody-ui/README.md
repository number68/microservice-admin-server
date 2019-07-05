spring-boot-admin-javamelody-ui
================================

### Building this module
The executable javamelody..js/javamelody..css **can be build with 'mvn clean install' command** with the frontend-maven-plugin. To do this node.js and npm will be installed on your machine.
If you don't want to use maven, please run the following commands(Node.js and npm must be installed on your machine and be on your `$PATH`):
### Build
```shell
npm install
npm run build
```

### As a result
javamelody..js/javamelody..css will be placed in directory microservice-admin-backend/src/main/resources/META-INF/spring-boot-admin-server-ui/extensions/javamelody or you can change it in vue.config.js.
