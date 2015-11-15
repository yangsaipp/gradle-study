package com.comtop.cap.component.loader.config;

import java.io.IOException;
import java.util.Properties;

import org.apache.commons.lang3.StringUtils;

/**
 * loader config factory 用于构建不同config
 * @author yangsai
 */
public class LoaderConfigFactory {
	
	/**
	 * 获取classpath中upload.properties文件内容并构建成config对象
	 * @return loaderConfig
	 */
	public static LoaderConfig createConfigFromProperties() {
		Properties prop = new Properties();
		try {
			prop.load(LoaderConfigFactory.class.getClassLoader().getResourceAsStream("cip.properties"));
			String basePath = (String) prop.get("basepath");
			if(StringUtils.isNoneBlank(basePath)){
				return new LoaderConfig(basePath);
			}
			
			String host = (String) prop.get("host");
			String username = (String) prop.get("username");
			String password = (String) prop.get("password");
			int port = Integer.parseInt((String)prop.get("port"));
			String encoding = (String) prop.get("encoding");
			String mainDirectory = (String) prop.get("mainDirectory");
			return new LoaderConfig(host, username, password, port, encoding, mainDirectory);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
}
