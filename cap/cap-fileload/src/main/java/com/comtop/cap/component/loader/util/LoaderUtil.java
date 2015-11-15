package com.comtop.cap.component.loader.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

import com.comtop.cap.component.loader.LoaderFactory;
import com.comtop.cap.component.loader.config.LoaderConfig;
import com.comtop.cap.component.loader.config.LoaderConfigFactory;

/**
 * loader 工具类
 * @author yangsai
 *
 */
public class LoaderUtil {
	/** 系统配置 */
	private static LoaderConfig sysLoaderConfig = LoaderConfigFactory.createConfigFromProperties();
	
	/**
	 * 上传附件到对应的文件夹下
	 * @param inputStream	上传资源的IO
	 * @param folderPath	上传后文件存放的文件夹路径
	 * @param fileName		上传后文件存放的名称
	 */
	public static void upLoad(InputStream inputStream, String folderPath, String fileName) {
		LoaderFactory.createLoader(sysLoaderConfig).upload(inputStream, folderPath, fileName);
	}
	
	/**
	 * 上传附件到对应的文件夹下
	 * @param file   	 上传资源
	 * @param folderPath 上传后文件存放的文件夹路径
	 * @param fileName   上传后文件存放的名称
	 */
	public static void uoload(File file, String folderPath, String fileName) {
		try {
			upLoad(new FileInputStream(file), folderPath, fileName);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 附件下载
	 * @param outputStream  下载资源的IO
	 * @param folderPath	下载的文件所在的文件夹路径
	 * @param fileName	            要下载的文件名称
	 */
	public static void downLoad(OutputStream outputStream, String folderPath, String fileName) {
		LoaderFactory.createLoader(sysLoaderConfig).downLoad(outputStream, folderPath, fileName);
	}
	
	/**
	 * 附件下载
	 * @param file  		下载存放的file对象
	 * @param folderPath	下载的文件所在的文件夹路径
	 * @param fileName	            要下载的文件名称
	 */
	public static void downLoad(File file, String folderPath, String fileName) {
		try {
			downLoad(new FileOutputStream(file), folderPath, fileName);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
}
