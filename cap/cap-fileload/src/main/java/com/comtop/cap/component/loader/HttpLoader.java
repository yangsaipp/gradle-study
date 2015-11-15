package com.comtop.cap.component.loader;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.commons.lang3.StringUtils;

import com.comtop.cap.component.loader.config.LoaderConfig;
import com.comtop.cap.component.loader.exception.LoadException;

/**
 * http 上传方式
 * @author sai.yang
 *
 */
public class HttpLoader implements Loadable {
	/** 单例对象 */
	static final HttpLoader instance = new HttpLoader();
	
	/** 加载配置对象  */
	private LoaderConfig config; 
	
	/** 构造函数 */
	HttpLoader() {
		super();
	}
	
	@Override
	public void downLoad(OutputStream outputStream, String folderPath, String fileName) {
		openServer();
		batchDownLoad(outputStream, folderPath, fileName);
		closeServer();
	}
	
	@Override
	public void upload(InputStream inputStream, String folderPath, String fileName) {
		openServer();
		batchUpload(inputStream, folderPath, fileName);
		closeServer();
	}

	@Override
	public void delete(String folderPath, String fileName) {
		openServer();
		batchDelete(folderPath, fileName);
		closeServer();
	}
	
	@Override
	public void configure(LoaderConfig loaderConfig) {
		this.config = loaderConfig;
	}

	@Override
	public void closeServer() {
		//empty
	}
	
	@Override
	public void openServer() {
		if(this.config == null || StringUtils.isBlank(this.config.getBasePath())) {
			throw new LoadException("http上传下载未配置basePath.");
		}
		
		String contextPath = (LoaderHelper.replacePathSeparator(this.config.getBasePath()));
		//要是结尾有/那么就去掉/
		if(contextPath.endsWith(LoaderHelper.separator)) {
			contextPath = contextPath.substring(0, contextPath.length() - 1);
		}
		this.config.setBasePath(contextPath);
	}

	@Override
	public void batchDelete(String folderPath, String fileName) {
		File file = LoaderHelper.getFile(getFullFolderPath(folderPath), fileName);
		if(file.exists()) {
			file.delete();
		}
	}

	@Override
	public void batchDownLoad(OutputStream outputStream, String folderPath,	String fileName) {
		InputStream inputStream = null;
		try {
			folderPath = getFullFolderPath(folderPath);
			inputStream = new BufferedInputStream(new FileInputStream(LoaderHelper.getFile(folderPath, fileName)));
			LoaderHelper.load(inputStream, outputStream);
		
		} catch (FileNotFoundException e) {
			throw new LoadException("服务器上文件" + fileName + "不存在，文件路径为：" + folderPath);
		} catch (IOException e) {
//			e.printStackTrace();
			throw new LoadException("下载出错!");
		}finally {
//			LoaderHelper.close(outputStream);	//放到外层UploadUtil去关闭
			LoaderHelper.close(inputStream);
		}
	}

	@Override
	public void batchUpload(InputStream inputStream, String folderPath, String fileName) {
		//检查文件夹路径是否存在,不存在就创建
		folderPath = getFullFolderPath(folderPath);
		File folderFile = new File(folderPath);
		if(!folderFile.exists()){
			folderFile.mkdirs();
		}
		OutputStream os = null;
		try {
			// 建立一个上传文件的输出流
			os = new BufferedOutputStream(new FileOutputStream(LoaderHelper.getFile(folderPath, fileName)));
			LoaderHelper.load(inputStream, os);
			
		} catch (FileNotFoundException e) {		//文件无法创建
//			e.printStackTrace();
			throw new LoadException("服务器上创建文件" + fileName + "失败，文件创建路径为：" + folderPath);
		} catch (IOException e) {				
//			e.printStackTrace();
			throw new LoadException("上传出错!");
		} finally {
			//close
//			LoaderHelper.close(inputStream);		//放到外层UploadUtil去关闭
			LoaderHelper.close(os);
		}
	}

	/**
	 * 
	 * @Methodname: getFullFolderPath
	 * @Discription: 获取全路径(config里面有contextPath那么就会加上没有就不加)
	 * @param folderPath 路径
	 * @return 全路径
	 *
	 */
	private String getFullFolderPath(String folderPath) {
		if(StringUtils.isNotBlank(config.getBasePath())) {
			folderPath = LoaderHelper.replacePathSeparator(folderPath);
			
			if(!folderPath.startsWith(LoaderHelper.separator)) {
				return config.getBasePath() + LoaderHelper.separator + folderPath;
			}
			return config.getBasePath() + folderPath;
		}
		return folderPath;
	}

	/**
	 * get config
	 * @return 加载配置
	 */
	public LoaderConfig getConfig() {
		return config;
	}
	
	/**
	 * 获取路径下的文件列表
	 * @param folderPath 路径
	 * @return 文件列表
	 */
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
		folderPath = getFullFolderPath(folderPath);
		File file = new File(folderPath);
		if(file.exists()&&file.isDirectory()){
			return file.list();
		}
		return null;
	}
}
