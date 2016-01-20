### UEditor 集成 UPYUN 标准 API

基于 [UEditor 1.4.3.1 PHP 版](http://ueditor.baidu.com)和 [UPYUN PHP 标准 SDK](https://github.com/upyun/php-sdk) 开发。

#### 功能

1. 单图和多图同时上传到 UPYUN
2. 附件同时上传到 UPYUN
3. 支持 log 记录

#### 使用

下载后解压为 `ueditor`（不支持其它的项目名称）

修改文件 `php/upyun.config.sample.php` 为 `php/upyun.config.php`，并配置：

```
<?php
$bucketname = '';   // 服务名称
$username = '';     // 操作员账号
$password = '';     // 操作员密码
```

修改文件 `php/config.sample.json` 为 `php/config.json`，并配置：

```
/* 上传图片配置项 */
……
"imageUrlPrefix": "", /* 图片访问路径前缀，UPYUN 域名配置，默认域名，如：http://服务名称.b0.upaiyun.com 或绑定域名，如：http://xxxx.xxxx.xxxx */
……
/* 上传文件配置 */
……
"fileUrlPrefix": "", /* 文件访问路径前缀，UPYUN 域名配置，默认域名，如：http://服务名称.b0.upaiyun.com 或绑定域名，如：http://xxxx.xxxx.xxxx */
```

说明：上传到 UPYUN 失败，可以查看 `php/log.txt` 文件里的详细错误信息。
