## 更新说明

1、增加UEditor java后台，基于UEditor 1.4.3 jsp修改

### java版使用说明：
1、将resource路径中 `config.upyun.sample.properties` 更改为 `config.upyun.properties`
2、配置 config.upyun.properties 中的又拍云存储授权信息

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
3、基于Spring-WebMVC的可直接使用本项目的UEditorController,或使用原项目的Controller.jsp