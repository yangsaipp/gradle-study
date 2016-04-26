package com.comtop.cap.component.loader;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 用于存放一些Loader通用的方法<br>
 * 比如关闭IO流 
 * @author sai.yang
 *
 */
public class LoaderHelper {
	/** 通用路径分隔符 */
	public final static String separator = "/";
	
	/**
	 * 通过给定路径和文件名生成对应的file<br>
	 * 实现了<code>Loadable</code>接口的类在获取路径的时候优先调用该方法获取文件路径
	 * @param folderPath	文件夹路径
	 * @param fileName		文件名
	 * @return File			
	 */
	public static File getFile(String folderPath, String fileName) {
		File file = new File(getFilePath(folderPath, fileName));
		return file;
	}
	
	/**
	 * 通过给定路径和文件名生成对应的路径<br>
	 * @param folderPath 文件夹路径
	 * @param fileName   文件名
	 * @return 文件路径
	 */
	public static String getFilePath(String folderPath, String fileName) {
		String filePath = "";
		folderPath = replacePathSeparator(folderPath);
		if(folderPath.endsWith(separator)) {
			filePath = (folderPath + fileName);
		}else {
			filePath = (folderPath + separator + fileName);
		}
		
		return filePath;
	}
	
	/**
	 * 替掉文件夹路径里面的\,统一采用/来表示路径
	 * @param folderPath 文件夹路径
	 * @return 替换后的文件夹路径
	 */
	public static String replacePathSeparator(String folderPath) {
		//替换掉文件夹路径里面的\,统一采用/来表示路径
		if(folderPath.indexOf("\\") != -1) {
			folderPath = folderPath.replaceAll("\\\\", separator);
		}
		//去掉开头的/
//		if(folderPath.startsWith(separator)) {
//			folderPath = folderPath.substring(1, folderPath.length());
//		}
		return folderPath;
	}
	
	/**
	 *  从inputStream里面写到outputStream里面去，注意该方法不会关闭输入流和输出流<br>
	 *  上传下载时调用，每次读写的大小为1024<br>
	 *  等价于调用 load(inputStream, outputStream, 1024)
	 * @param inputStream	要读入的iuputStream
	 * @param outputStream	要写出的outputStream
	 * @return 读写的文件大小
	 * @throws IOException ioexception
	 */
	public static long load(InputStream inputStream, OutputStream outputStream) throws IOException {
		return load(inputStream, outputStream, 1024);
	}
	
	/**
	 * 从inputStream里面写到outputStream里面去，注意该方法不会关闭输入流和输出流<br>
	 * http上传下载时调用
	 * @param inputStream	要读入的iuputStream
	 * @param outputStream	要写出的outputStream
	 * @param buffSize		每次读入的字节大小
	 * @return				读写的文件大小
	 * @throws IOException ioexception
	 */
	public static long load(InputStream inputStream, OutputStream outputStream, int buffSize) throws IOException {
		long size = 0L;
		
		int c;
		byte[] bytes = new byte[buffSize]; 
		while ((c = inputStream.read(bytes, 0, bytes.length)) != -1) {   
			outputStream.write(bytes, 0, c);   
			size = size + c;   
		}
		outputStream.flush();
		
//		close(outputStream);
//		close(inputStream);

		return size;
	}
	
	/**
	 * 通过给定的folderPath获取到对应的每一个文件夹名称的list
	 * @param folderPath 文件夹路径
	 * @return 文件夹名称的list
	 */
	public static List<String> getFolderPathList(String folderPath) {
		folderPath = replacePathSeparator(folderPath);
		String[] folderPathArray = folderPath.split(separator);
		List<String> folderPathList = new ArrayList<String>(folderPathArray.length);
		Collections.addAll(folderPathList, folderPathArray);
		return folderPathList;
	}
	
	/**
	 * 关闭inputStream
	 * @param inputStream 输入流
	 */
	public static void close(InputStream inputStream) {
		if(inputStream != null) {
			try {
				inputStream.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * 关闭outputStream
	 * @param outputStream 输出流
	 */
	public static void close(OutputStream outputStream) {
		if(outputStream != null) {
			try {
				outputStream.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
}
