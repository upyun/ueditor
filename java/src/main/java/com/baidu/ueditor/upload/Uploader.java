package com.baidu.ueditor.upload;

import com.baidu.ueditor.define.ActionMap;
import com.baidu.ueditor.define.BaseState;
import com.baidu.ueditor.define.State;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.Properties;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import main.java.com.UpYun;

public class Uploader {
	private static final Logger log = LoggerFactory.getLogger(Uploader.class);
	private HttpServletRequest request = null;
	private Map<String, Object> conf = null;
	private static final String upyunConfig = "/config.upyun.properties";
	private Properties upyunProperties = null;

	public Uploader(HttpServletRequest request, Map<String, Object> conf) {
		this.request = request;
		this.conf = conf;
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

	public final State doExec() {
		String filedName = (String) this.conf.get("fieldName");
		State state = null;

		if ("true".equals(this.conf.get("isBase64"))) {
			state = Base64Uploader.save(this.request.getParameter(filedName),
					this.conf);
		} else {
			state = BinaryUploader.save(this.request, this.conf);
		}

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
					String spaceName = "";
					String opName = "";
					String opPassowrd = "";
					if (actionCode == ActionMap.UPLOAD_IMAGE
							|| actionCode == ActionMap.UPLOAD_SCRAWL) {
						spaceName = upyunProperties.getProperty("img_bucket",
								"");
						opName = upyunProperties
								.getProperty("img_username", "");
						opPassowrd = upyunProperties.getProperty("img_passwd",
								"");

					} else if (actionCode == ActionMap.UPLOAD_FILE
							|| actionCode == ActionMap.UPLOAD_VIDEO) {
						spaceName = upyunProperties.getProperty("file_bucket",
								"");
						opName = upyunProperties.getProperty("file_username",
								"");
						opPassowrd = upyunProperties.getProperty("file_passwd",
								"");

					} else {
						log.error("Unexpected AcitonCode, not image and file!");
					}

					try {
						UpYun upyun = new UpYun(spaceName, opName, opPassowrd);
						File file = new File(physicalPath);
						upyun.setContentMD5(UpYun.md5(file));
						boolean ret = upyun.writeFile(savePath, file, true);
						if (ret) {
							// 上传成功

						} else {
							throw new IOException("Upload to upyun fail!");
						}
					} catch (IOException e) {
						// e.printStackTrace();
						log.error("Upload to upyun fail!", e);
					} catch (Exception e) {
						log.error("Unexpected exceptions", e);
					}
				}
			}
		}

		return state;
	}
}
