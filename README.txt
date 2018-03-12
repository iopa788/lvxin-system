1.cim-android-sdk 是Android客户端sdk源码。如果不修改不需要导入，
2.cim-server-sdk 是服务端sdk源码。如果不修改不需要导入
3.lvxin-server是服务端工程，是eclipse4.x 基于gradle  jar包管理工具(最新版Eclipse J2EE版本 安装gradle插件详见)

https://github.com/eclipse/buildship/blob/master/docs/user/Installation.md

4.client-lvxin-android 是android客户端工程，开发工具是android studio
5.LvxinAccountServer是公众号服务端的demo，是一个spring boot项目，基于gradle 最新版Eclipse 安装gradle插件详见
https://github.com/eclipse/buildship/blob/master/docs/user/Installation.md

6.doc 是相关文档

-----------------需要修改的一些配置---------------------------
1.服务端数据库配置在lvxin-server\src\main\resource\jdbc.propertis里面
2.客户端配置服务端的IP 端口信息 在客户端的Constant.java里面
3 关于文件存储，目前是存在自己服务器工程下的files文件下,可以改成第三方云存储如阿里云 OSS等，可在客户端修改CloudFileUploader和CloudFileDownloader以及FileURLBuilder即可

-----------------服务端搭建---------------------------
1 安装jdk1.8 和tomcat 7,以及相关数据库 MySql， SQLServer 或者Oracle都可以
2 eclipse J2EE版本(下载地址:http://www.eclipse.org/downloads/packages/eclipse-ide-java-ee-developers/neon2)
   导入lvxin-server，js或jsp的语法错误提示忽略
