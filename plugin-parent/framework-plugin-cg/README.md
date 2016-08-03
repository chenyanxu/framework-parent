# framework-plugin-cg

##### 利用Apache的mvn plugin开发的新功能模块代码自动生成插件
  * api包是通用功能的接口类
  * impl包是代码的实现类
  * Mojo文件是实现org.apache.maven.plugin.Mojo接口的实现类
  * resources\templates目录下的.st文件是代码的模板文件

# 使用
调用时先提供java bean文件及pom.xml文件，然后在pom.xml目录内执行mvn命令，使用示例如下：

###### 已配置mvn(M2_HOME)环境变量的情况

    C:\Users\Administrator_>mvn frameworkcg:create-all
###### 未配置mvn环境变量的情况,其中D:\java-develop\tools\apache-karaf-4.0.5-new\data\tmp\cgt\StudentBean为pom.xml和java bean文件所在的目录

    C:\Users\Administrator_>D:\java-develop\tools\apache-maven-3.3.9\bin\mvn -f D:\java-develop\tools\apache-karaf-4.0.5-new\data\tmp\cgt\StudentBean frameworkcg:create-all

# 注意事项
  * 生成的代码有乱码
    * 利用mvn方式调用该插件时，需要在与pom.xml文件相同目录下建立一个.mvn文件夹，然后在.mvn目录里新建jvm.config文件，文件内容为

            -Dfile.encoding=UTF-8

  * 插件的调试
    * 客户的的配置
      * Edit Configurations...
      * 在弹出的界面中点"+"号,选择Maven
       * 在右侧Name中输入名称,在Parameter选项卡中的Command Line:中输入

                frameworkcg:create-all

       还可以在Runner选项卡中输入端口号等

      * 点Apply，点OK
      * 在Maven Projects 中找到Plugins中找到frameworkcg下的frameworkcg:create-all
      * 双击上述即可运行

    * 插件端的配置
      * Edit Configurations...
        * 在弹出的界面中点"+"号,选择Remote
        * 在右侧Name中输入名称,在Configuration选项卡下填写与客户端相同的Port
        * 点Apply，点OK
        * 点运行调试按钮,放好断点，进行调试即可。
    * 先运行客户端，再运行插件端