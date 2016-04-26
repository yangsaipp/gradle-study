package com.comtop.cap.component.loader;

import com.comtop.cap.component.loader.config.LoaderConfig;

/**
 * loader工厂类 用于创建loader
 * 
 * @author sai.yang
 * 
 */
public class LoaderFactory {
	
	/**
	 * 根据配置创建对应的Loader
	 * @param config 上传下载配置
	 * @return Loadable
	 */
	public static Loadable createLoader(LoaderConfig config) {
		switch (config.getLoaderType()) {
		case HTTP:
			return createHttpLoader(config);
		case FTP:
			return createFtpLoader(config);
		default:
			return null;
		}
	}
	
	/**
	 * 创建 http loader，注意这里返回的始终是同一个对象
	 * @param config 上传下载配置
	 * @return loadable
	 */
	public static Loadable createHttpLoader(LoaderConfig config) {
		HttpLoader.instance.configure(config);
		return HttpLoader.instance;
	}

	/**
	 * 
	 * @Methodname createFtpLoader
	 * @Discription 使用loaderConfig创建ftp loader,注意这里每次都会创建一个新的loader
	 * @param config 上传下载配置
	 * @return loadable
	 */
	public static Loadable createFtpLoader(LoaderConfig config) {
		return new ApacheFtpLoader(config);
	}
	
}
