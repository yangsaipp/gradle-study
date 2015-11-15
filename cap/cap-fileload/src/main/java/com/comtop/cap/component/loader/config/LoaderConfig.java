package com.comtop.cap.component.loader.config;

import com.comtop.cap.component.loader.LoaderType;

/**
 * loader的相关配置信息,比如 ip, userName, password等
 * @author sai.yang
 *
 */
public class LoaderConfig {
	//http 相关设置,在web项目中一般的值为getSession().getServletContext().getRealPath("/");
	/**即项目部署后的服务器路径，http上传下载都是基于该目录 */
	private String basePath;
	
	//ftp 设置
	/** ftp主机ip */
	private String host;  					
	/** ftp用户名*/
	private String username;
	/** ftp密码*/
	private String password;
	/** ftp端口*/
	private int port;
	/** ftp编码*/
	private String encoding;
	/** ftp上传下载主目录*/
	private String mainDirectory;
	
	/** 默认ftp端口 */
	private final static int default_port = 21;
	/** 默认ftp编码 */
	private final static String default_encoding = "utf-8";
	/** 默认ftp主目录 */
	private final static String default_mainDirectory = "/";
	
	/** 配置文件类型 */
	private LoaderType loaderType;
	
	/**
	 * 构造方法
	 */
	public LoaderConfig(){
		super();
	}
	
	/**
	 * 构造方法
	 * @param basePath http上传基础目录
	 */
	public LoaderConfig(String basePath){
		super();
		this.basePath = basePath;
		loaderType = LoaderType.HTTP;
	}
	
	/**
	 * 用给定的参数初始化一个loader config<br>
	 * 初始化的config的port为80, encoding为utf-8, mainDirectory为"/"
	 * @param host ftp主机ip	
	 * @param username ftp用户名
	 * @param password ftp密码
	 */
	public LoaderConfig(String host, String username, String password) {
		this(host, username, password, default_port, default_mainDirectory);
	}
	
	/**
	 * 用给定的参数初始化一个loader config<br>
	 * 初始化的config的port为80, mainDirectory为"/"
	 * @param host ftp主机ip	
	 * @param username ftp用户名
	 * @param password ftp密码
	 * @param mainDirectory ftp上传下载主目录
	 */
	public LoaderConfig(String host, String username, String password, String mainDirectory) {
		this(host, username, password, default_port, mainDirectory);
	}

	/**
	 * 用给定的参数初始化一个loader config<br>
	 * 初始化的config的mainDirectory为"/"
	 * @param host ftp主机ip	
	 * @param username ftp用户名
	 * @param password ftp密码
	 * @param port ftp端口
	 * @param mainDirectory ftp上传下载主目录
	 */
	public LoaderConfig(String host, String username, String password, int port, String mainDirectory) {
		this(host, username, password, default_port, default_encoding, mainDirectory);
	}
	
	/**
	 * 用给定的参数初始化一个loader config
	 * @param host ftp主机ip	
	 * @param username ftp用户名
	 * @param password ftp密码
	 * @param port ftp端口
	 * @param encoding ftp编码
	 * @param mainDirectory ftp上传下载主目录
	 */
	public LoaderConfig(String host, String username, String password, int port, String encoding, String mainDirectory) {
		super();
		this.host = host;
		this.username = username;
		this.password = password;
		this.port = port;
		this.encoding = encoding;
		this.mainDirectory = mainDirectory;
		loaderType = LoaderType.FTP;
	}

	/**
	 * get ftp端口
	 * @return ftp 端口
	 */
	public int getPort() {
		return port;
	}

	/**
	 * set ftp端口
	 * @param port 端口
	 */
	public void setPort(int port) {
		this.port = port;
	}
	
	/**
	 * get ftp用户名
	 * @return ftp用户名
	 */
	public String getUsername() {
		return username;
	}

	/**
	 * set ftp用户名
	 * @param username ftp用户名
	 */
	public void setUsername(String username) {
		this.username = username;
	}

	/**
	 * get ftp密码
	 * @return ftp密码
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * set ftp密码
	 * @param password ftp密码
	 */
	public void setPassword(String password) {
		this.password = password;
	}

	/**
	 * get ftp编码
	 * @return ftp编码 
	 */
	public String getEncoding() {
		return encoding;
	}

	/**
	 * set ftp编码
	 * @param encoding ftp编码
	 */
	public void setEncoding(String encoding) {
		this.encoding = encoding;
	}

	/**
	 * get ftp主机ip
	 * @return ftp主机ip
	 */
	public String getHost() {
		return host;
	}

	/**
	 * set ftp主机ip
	 * @param host ftp主机ip
	 */
	public void setHost(String host) {
		this.host = host;
	}

	/**
	 * get ftp主机ip
	 * @return ftp主机ip
	 */
	public String getMainDirectory() {
		return mainDirectory;
	}

	/**
	 * set ftp上传下载主目录
	 * @param mainDirectory ftp上传下载主目录
	 */
	public void setMainDirectory(String mainDirectory) {
		this.mainDirectory = mainDirectory;
	}

	/**
	 * get http上传下载主目录
	 * @return http上传下载主目录
	 */
	public String getBasePath() {
		return basePath;
	}

	/**
	 * set http上传下载主目录
	 * @param basePath http上传下载主目录
	 */
	public void setBasePath(String basePath) {
		this.basePath = basePath;
	}
	
	/**
	 * 判断是否是ftp加载配置
	 * @return true 是  false 否
	 */
	public boolean isFTP() {
		return loaderType == null ? false : loaderType.isFTP();
	}
	
	/**
	 * 判断是否是http加载配置
	 * @return true 是  false 否
	 */
	public boolean isHTTP() {
		return loaderType == null ? false : loaderType.isHTTP();
	}

	/**
	 * get loader type
	 * @return loaderType
	 */
	public LoaderType getLoaderType() {
		return loaderType;
	}

	/**
	 * set loader type
	 * @param loaderType loader type
	 */
	public void setLoaderType(LoaderType loaderType) {
		this.loaderType = loaderType;
	}
	
}
