package com.faxsun.common.web.controller;

import java.lang.reflect.Field;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.baidu.ueditor.ActionEnter;
import com.baidu.ueditor.ConfigManager;

@Controller
@RequestMapping("/ueditor")
public class UEditorController {

	@RequestMapping(value = "/dispatch", produces = "application/json")
	@ResponseBody
	public String dispatch(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		request.setCharacterEncoding("utf-8");

		String rootPath = request.getServletContext().getRealPath("/");
		ActionEnter ae = new ActionEnter(request, rootPath);

		// ueditor未提供设置configManager的方式
		// 在原jsp实现方式中，配置文件相对于jsp路径写死：配置文件与jsp在同一目录下
		// 此处采用反射方式设置路径
		@SuppressWarnings("rawtypes")
		Class clazz = ActionEnter.class;

		Field field = clazz.getDeclaredField("configManager");
		field.setAccessible(true);
		// 构造config.json所在路径,"/WEB-INF"为配置文件所在路径，可以根据实际情况修改
		field.set(ae, ConfigManager.getInstance(rootPath, "", "/WEB-INF"));

		return ae.exec();
	}
}
