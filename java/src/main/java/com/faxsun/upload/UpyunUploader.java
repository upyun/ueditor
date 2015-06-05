package com.faxsun.upload;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import main.java.com.UpYun;

import com.baidu.ueditor.define.ActionMap;
import com.baidu.ueditor.define.BaseState;
import com.baidu.ueditor.define.State;
import com.baidu.ueditor.upload.Uploader;

/**
 * Upyun上传图片/文件
 * 
 * @author zhaoteng.song@faxsun.com 2015年6月5日
 *
 */
public class UpyunUploader {

	private static final Logger log = LoggerFactory
			.getLogger(UpyunUploader.class);

	private static final String upyunConfig = "/config.upyun.properties";
	private static Properties upyunProperties = null;

	static {
		InputStream in = Uploader.class.getResourceAsStream(upyunConfig);
		if (in != null) {
			upyunProperties = new Properties();
			try {
				upyunProperties.load(in);
			} catch (IOException e) {
				log.error("", e);
			} finally {
				try {
					in.close();
				} catch (IOException e) {
				}
			}
		}
	}

	public static void upload(State state, Map<String, Object> conf) {
		if (state.isSuccess()) {
			BaseState baseState = (BaseState) state;
			/**
			 * map <br/>
			 * { "url" => relative path of saved file <br/>
			 * "type" => suffix of the uploaded file <br/>
			 * "original" => original filename, especially "" for Base64Uploader <br/>
			 * }
			 */
			Map<String, String> infoMap = baseState.getInfoMap();

			String savePath = infoMap.get("url");
			if (savePath != null && !savePath.isEmpty()) {
				/*
				 * @ L64 of Base64Uploader
				 * 
				 * @ L72 of BinaryUploader
				 */
				String physicalPath = (String) conf.get("rootPath") + savePath;

				Integer actionCode = (Integer) conf.get("actionCode");
				if (actionCode == null) {
					log.error("Uploader is unable to get actionCode");
				} else {

					UpyunConfig config = readConfigDependOnActionType(actionCode);

					try {
						UpYun upyun = new UpYun(config.spaceName,
								config.opName, config.opPassword);
						File file = new File(physicalPath);
						upyun.setContentMD5(UpYun.md5(file));
						boolean ret = upyun.writeFile(savePath, file, true);
						if (ret) {
							// 上传成功

						} else {
							throw new IOException("Upload to upyun fail!");
						}
					} catch (IOException e) {
						log.error("Upload to upyun fail!", e);
					} catch (Exception e) {
						log.error("Unexpected exceptions", e);
					}
				}
			}
		}
	}

	/**
	 * 根据执行的动作，读取upyun配置。图片类型操作使用图片空间配置，其他使用文件空间配置
	 * @param actionCode
	 * @return
	 */
	private static UpyunConfig readConfigDependOnActionType(int actionCode) {
		UpyunConfig config = new UpyunConfig();
		if (actionCode == ActionMap.UPLOAD_IMAGE
				|| actionCode == ActionMap.UPLOAD_SCRAWL
				|| actionCode == ActionMap.CATCH_IMAGE) {
			config.spaceName = upyunProperties.getProperty("img_bucket", "");
			config.opName = upyunProperties.getProperty("img_username", "");
			config.opPassword = upyunProperties.getProperty("img_passwd", "");

		} else if (actionCode == ActionMap.UPLOAD_FILE
				|| actionCode == ActionMap.UPLOAD_VIDEO) {
			config.spaceName = upyunProperties.getProperty("file_bucket", "");
			config.opName = upyunProperties.getProperty("file_username", "");
			config.opPassword = upyunProperties.getProperty("file_passwd", "");

		} else {
			log.error("Unexpected AcitonCode, not image and file!");
		}
		return config;
	}
}

class UpyunConfig {
	public String spaceName;
	public String opName;
	public String opPassword;
}