# 又拍云存储UpYun百度编辑器UEditor集成工具v1.0

基于最新版[UEditor1.2.6.1 PHP版本](http://ueditor.baidu.com/website/download.html#ueditor)和[又拍云PHP SDK](https://github.com/upyun/php-sdk)开发。

## 更新说明
* UEditor版本由1.2.5升级到1.2.6.1；
* 增加对远程图片存入又拍云的支持；
* 增加对图片搜索存入又拍云的支持。

## 使用须知
* 已经安装部署百度编辑器UEditor(没有安装部署，本项目也可独立运行)。
* 工具是基于又拍云存储服务的，使用要有有效的又拍云存储帐号，还没有帐号的，可以[点击免费注册帐号](https://www.upyun.com/intro/register.php)

## 安装说明
* 下载后解压，可得文件夹upyun-ueditor；
* 复制文件`upyun.config.php`、`upyun.class.php`、`ueditor.config.js`、`index.php`到所部署的UEditor根目录下，复制文件`php/fileUp.php`、`php/getRemoteImage.php`、`php/imageUp.php`到所部署的UEditor的php目录；
* 配置upyun.config.php中的又拍云存储授权信息
````
// 图片类空间  
$img_bucket = "";   // 空间名  
$img_username = ""; // 操作员帐号  
$img_passwd = "";   //操作员密码  

// 文件类空间  
$file_bucket = "";   // 空间名  
$file_username = ""; // 操作员帐号  
$file_passwd = "";   //操作员密码  
````

说明：如果不需要将图片和非图片类文件分空间存储，则只需将图片空间和文件空间相关信息填写成同一个文件类空间即可（必须为文件类空间）    

* 配置ueditor.config.js中的空间名
````
var img_bucket = "";    // 图片空间名   
var file_bucket = "";    // 文件空间名 
````

说明：如果不需要将图片和非图片类文件分空间存储，则只需将图片空间和文件空间相关信息填写成同一个文件类空间即可（必须为文件类空间）    
