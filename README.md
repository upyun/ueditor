# 又拍云存储 UpYun 百度编辑器 UEditor 集成工具 v1.1

基于最新版 [UEditor1.3.6 PHP 版本](http://ueditor.baidu.com/website/download.html#ueditor)和 [又拍云 PHP SDK](https://github.com/upyun/php-sdk) 开发。

## 更新说明
1、UEditor 版本由1.2.6.1升级到1.3.6；   

## 涉及又拍云的主要功能
1、支持"图片-本地上传"到又拍云；
2、支持"附件"上传到又拍云；
3、支持"图片-远程图片"上传到又拍云；
4、支持"图片-图片搜索"上传到又拍云;
5、支持 log 记录。

## 使用须知
1、已经安装部署百度编辑器 UEditor (没有安装部署，本项目也可独立运行), 如何部署参考[官方文档](http://fex.baidu.com/ueditor/);    
2、工具是基于又拍云存储服务的，使用要有有效的又拍云存储帐号，还没有帐号的，可以[点击免费注册帐号](https://www.upyun.com/cp/#/register/)。

## 安装使用说明
1、下载后解压，可得文件夹 ueditor-for-UPYUN；   
2、修改 `upyun.config.sample.php` 为 `upyun.config.php`，修改 `ueditor.config.sample.js` 为 `ueditor.config.js`, 复制文件 `upyun.config.php`、`upyun.class.php`、`ueditor.config.js`、`index.php` 到所部署的 UEditor 根目录下，复制文件 `php/fileUp.php`、`php/getRemoteImage.php`、`php/imageUp.php` 到所部署的 UEditor 的 php 目录；   
3、配置 upyun.config.php 中的又拍云存储授权信息

```
// 图片类空间
$img_bucket = ""; // 空间名
$img_username = ""; // 操作员帐号
$img_passwd = "";  // 操作员密码
// 文件类空间
$file_bucket = ""; // 空间名
$file_username = ""; // 操作员帐号
$file_passwd = ""; // 操作员密码
```

说明：如果不需要将图片和非图片类文件分空间存储，则只需将图片空间和文件空间相关信息填写成同一个文件类空间即可（必须为文件类空间）。

4、配置 ueditor.config.js 中的空间名

```
var img_bucket = ""; // 图片空间名
var file_bucket = ""; // 文件空间名
```

说明：如果不需要将图片和非图片类文件分空间存储，则只需将图片空间和文件空间相关信息填写成同一个文件类空间即可（必须为文件类空间）。

5、log 记录

可通过文件 `php/error_log.txt` 查看上传失败所返回的相关错误信息。
