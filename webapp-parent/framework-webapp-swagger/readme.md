## swagger for restful api
access to this url: http://localhost:8181/swagger/index.html

## create swagger.yaml or swagger.json 

地址栏中输入 http://localhost:8181/swagger/api-docs/userCamel/swagger.json 或者
http://localhost:8181/swagger/api-docs/userCamel/swagger.yaml

F12 在浏览器中就可以看到形成的swagger文件，复制出来就可以了。

## 获得camelContext的rest列表

http://localhost:8181/swagger/api-docs

## 源码下载以及更新
> 增加中文支持

```bash
<!-- Some basic translations -->
   <script src='lang/translator.js' type='text/javascript'></script>
  <!-- <script src='lang/ru.js' type='text/javascript'></script> -->
   <script src='lang/zh-cn.js' type='text/javascript'></script>
```

https://github.com/swagger-api/swagger-ui 
直接复制dist下的文件覆盖就可以了
修改index.html中的 url = "http://localhost:8181/swagger/api-docs/userCamel";

## 授权 
 index.html的地址栏中输入http://petstore.swagger.io/v2/swagger.json 测试显示了authorize 按钮
 
## swagger editor 
 https://github.com/swagger-api/swagger-editor

## camel doc
https://access.redhat.com/documentation/en/red-hat-jboss-fuse/6.3/paged/deploying-into-apache-karaf/chapter-15-camel-cdi

## 说明

web.xml里面的servlet和filter已经废弃，直接在restConfig里面通过方法实现了。
swagger-ui-3.22.2 的版本是从github上swagger的发布包的dist文件复制过来的，如果需要升级，下次直接升级相应的zip包即可。