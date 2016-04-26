package com.comtop.cap.component.loader;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.SocketException;
import java.util.List;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;

import com.comtop.cap.component.loader.config.LoaderConfig;
import com.comtop.cap.component.loader.exception.LoadException;



/**
 * FTP上传下载方式，即上传下载都在应用服务器<br>
 * 采用的是apache的ftpClient实现
 * @author sai.yang
 *
 */
public class ApacheFtpLoader implements Loadable {
	
	/** ApacheFtpLoader 对象 单例 */
//	public static final ApacheFtpLoader instance = new ApacheFtpLoader();
	
	/** 上传下载配置对象 */
	private LoaderConfig config;
	
	/** ftp路径分隔符	 */
	private final static String ftp_separator = "/";
	
	/** ftp client */
	private FTPClient ftpClient;
	
	/**
	 * 构造方法
	 */
	ApacheFtpLoader(){
		super();
	}
	
	/**
	 * 构造方法
	 * @param config 配置
	 */
	ApacheFtpLoader(LoaderConfig config){
		super();
		this.config = config;
	}
	
	@Override
	public void closeServer() {
		if (ftpClient.isConnected()) {
		    try {
		    	ftpClient.disconnect();
		    	//System.out.println("ftp断开");
		    } catch (IOException e) {
		    	//System.out.println("ftp关闭失败");
		    	e.printStackTrace();
		    }
		}
	}

	@Override
	public void openServer() {
		if(config == null) {
			throw new LoadException("找不到相关ftp连接配置参数,无法连接到ftp服务器！");
		}
		
		//要是ftpClient是活动的就不在创建连接
		if(ftpClient != null && ftpClient.isAvailable()) {
			return;
		}
		
		ftpClient = new FTPClient();
		
		//设置对中文文件名上传下载的支持
		ftpClient.setControlEncoding(config.getEncoding());  
//		FTPClientConfig conf = new FTPClientConfig(FTPClientConfig.SYST_NT);
//		ftpClient.configure(conf);
		
//		conf.setServerLanguageCode("zh");
		
		try {
			//连接远程ftp服务器
			if(config.getPort() == -1) {
				ftpClient.connect(config.getHost());
			}else {
				ftpClient.connect(config.getHost(), config.getPort());
			}
			//登录
			if(!ftpClient.login(config.getUsername(), config.getPassword())){
				throw new LoadException("ftp服务器登录失败，请检查连接的用户名和密码！");
			}
			
			ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
			ftpClient.changeWorkingDirectory(config.getMainDirectory());
			
			//System.out.println("登录成功！server 地址：" + config.getHost() + " port:" + config.getPort() );
		} catch (SocketException e) {
//			e.printStackTrace();
			throw new LoadException("ftp服务器连接失败，请检查连接配置。 " + getFtpConfig(config), e);
		} catch (IOException e) {
//			e.printStackTrace();
			throw new LoadException("ftp服务器连接失败，请检查连接配置。" + getFtpConfig(config), e);
		}
		
	}
	
	@Override
	public void configure(LoaderConfig loaderConfig) {
		this.config = loaderConfig;
	}
	
	@Override
	public void downLoad(OutputStream outputStream, String folderPath, String fileName) {
		//获取client
		openServer();
		try {
			String path = LoaderHelper.getFilePath(folderPath, fileName);
			if(!ftpClient.retrieveFile(path, outputStream)) {
				throw new LoadException("请求下载的资源不存在!资源文件为：" + ftpClient.printWorkingDirectory() + path);
			}
//			LoaderHelper.close(outputStream);	//放到外层UploadUtil去关闭
		} catch (IOException e) {
			e.printStackTrace();
			throw new LoadException("ftp下载出错!");
		} finally {
			//关闭连接
//			LoaderHelper.close(outputStream);	//放到外层UploadUtil去关闭
			closeServer();
		}
	}

	@Override
	public void upload(InputStream inputStream, String folderPath, String fileName) {
		//client
		openServer();
		
		try {
			//如果目录不存在
			if(!isDirectoryExists(folderPath)) {
				//创建
				mkd(folderPath);
			}
			
			if(!ftpClient.storeFile(LoaderHelper.getFilePath(folderPath, fileName), inputStream)) {
				throw new LoadException("ftp上传出错!请检查服务器上传目录是否存在：" +  ftpClient.printWorkingDirectory() + folderPath);
			}
//			LoaderHelper.close(inputStream);
		} catch (IOException e) {
			e.printStackTrace();
			throw new LoadException("ftp上传出错!");
		} finally {
			//关闭连接
			LoaderHelper.close(inputStream);
//			LoaderHelper.close(outputStream);	//放到外层UploadUtil去关闭
			closeServer();
		}
		
	}

	@Override
	public void delete(String folderPath, String fileName) {
		//获取client
		openServer();
		
		try {
			ftpClient.deleteFile(LoaderHelper.getFilePath(folderPath, fileName));
		} catch (IOException e) {
			e.printStackTrace();
			throw new LoadException("ftp上传出错!");
		} finally {
			//关闭连接
			closeServer();
		}
		
	}
	
	/**
	 * 判断对应的文件夹目录在服务器端是否存在
	 * @param folderPath 目录
	 * @return true 存在  false 不存在 
	 * @throws IOException IO异常
	 */
	public boolean isDirectoryExists(String folderPath) throws IOException {
        if(ftp_separator.equals(folderPath)) {
        	return true;
        }
        
		FTPFile[] ftpFileArr = ftpClient.listDirectories(folderPath);
      /*  for(FTPFile f : ftpFileArr) {
        	//System.out.println(f.getName());
        }*/
        return ftpFileArr.length > 0;
	}
	
	/**
	 * 通过给定的文件夹路径创建文件夹(文件定位的开始是用当前的工作目录)<br>
	 * 要是父文件夹不存在也会创建父文件夹
	 * @param folderPath 目录
	 * @throws IOException IO异常
	 */
	public void mkd(String folderPath) throws IOException {
		//获取到文件夹路径中每个文件夹的名称
		List<String> folderList = LoaderHelper.getFolderPathList(folderPath);
		//获取ftp当前的工作目录
		String ftpWorkingDirectory = ftpClient.printWorkingDirectory();
		
		for(String folder : folderList) {
			//如果不存在就创建
			if(!isDirectoryExists(folder)) {
				ftpClient.makeDirectory(folder);
				//System.out.println("创建目录：" + folder);
			}
			//并切换为当前的工作目录
			ftpClient.changeWorkingDirectory(folder);
		}
		//创建完成后切换回原来的工作目录
		ftpClient.changeWorkingDirectory(ftpWorkingDirectory);
	}

	@Override
	public void batchDelete(String folderPath, String fileName) {
		try {
			ftpClient.deleteFile(LoaderHelper.getFilePath(folderPath, fileName));
		} catch (IOException e) {
			e.printStackTrace();
			throw new LoadException("ftp上传出错!");
		}
	}

	@Override
	public void batchDownLoad(OutputStream outputStream, String folderPath,	String fileName) {
		try {
			String path = LoaderHelper.getFilePath(folderPath, fileName);
			if(!ftpClient.retrieveFile(path, outputStream)) {
				throw new LoadException("请求下载的资源不存在!资源文件为：" + ftpClient.printWorkingDirectory() + path);
			}
//			LoaderHelper.close(outputStream);	//放到外层UploadUtil去关闭
		} catch (IOException e) {
			e.printStackTrace();
			throw new LoadException("ftp下载出错!");
		}
	}

	@Override
	public void batchUpload(InputStream inputStream, String folderPath, String fileName) {
		try {
			//如果目录不存在
			if(!isDirectoryExists(folderPath)) {
				//创建
				mkd(folderPath);
			}
			
			if(!ftpClient.storeFile(LoaderHelper.getFilePath(folderPath, fileName), inputStream)) {
				throw new LoadException("ftp上传出错!请检查服务器上传目录是否存在：" +  ftpClient.printWorkingDirectory() + folderPath);
			}
//			LoaderHelper.close(outputStream);	//放到外层UploadUtil去关闭
		} catch (IOException e) {
			e.printStackTrace();
			throw new LoadException("ftp上传出错!");
		}
	}

	@Override
	public String[] getFileNamesFromFolder(String folderPath){
		openServer();
		String[] names = getFileNames(folderPath);
		closeServer();
		return names;
	}
	
	/**
	 * 获取路径下的文件列表
	 * @param folderPath 路径
	 * @return 文件列表
	 */
	private String[] getFileNames(String folderPath){
		try {
			return ftpClient.listNames(folderPath);
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	/**
	 * 获取config中ftp的信息
	 * @param loaderConfig 配置
	 * @return ftp的配置信息
	 */
	public String getFtpConfig(LoaderConfig loaderConfig) {
		return "host:" + loaderConfig.getHost()
				+ " username:" + loaderConfig.getUsername()
				+ " password:" + loaderConfig.getPassword()
				+ " port:" + loaderConfig.getPort();
	}
}
