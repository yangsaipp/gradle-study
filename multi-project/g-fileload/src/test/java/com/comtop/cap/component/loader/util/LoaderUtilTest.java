package com.comtop.cap.component.loader.util;

import java.io.File;

import org.junit.Test;

/**
 * LoaderUtil测试
 * @author yangsai
 */
public class LoaderUtilTest {
	
	/** 测试文件上传后存放的路径 */
	private String folderPath = "testUploadFile";
	/** 测试上传文件存放的路径 */
	private String uploadPath = "uploadFile";
	/** 测试下载文件存放的路径 */
	private String downloadPath = "downloadFile";
	
	/**
	 * 测试 upload方法
	 */
	@Test
	public void testUpload() {
		LoaderUtil.upLoad(LoaderUtilTest.class.getClassLoader().getResourceAsStream(uploadPath + "/数据梳理.txt"), folderPath, "test.txt");
		LoaderUtil.upLoad(LoaderUtilTest.class.getClassLoader().getResourceAsStream(uploadPath + "/test_图片.gif"), folderPath, "test_图片.gif");
		LoaderUtil.upLoad(LoaderUtilTest.class.getClassLoader().getResourceAsStream(uploadPath + "/test_图片.png"), folderPath, "test_图片.png");
	}

	/**
	 * 测试下载方法
	 */
	@Test
	public void testDownload() {
		String classPath = LoaderUtilTest.class.getClassLoader().getResource("").getPath();
		LoaderUtil.downLoad(new File(classPath + downloadPath + "/数据梳理.txt"), folderPath, "test.txt");
		LoaderUtil.downLoad(new File(classPath + downloadPath + "/test_图片.gif"), folderPath, "/test_图片.gif");
		LoaderUtil.downLoad(new File(classPath + downloadPath +"/test_图片.png"), folderPath, "/test_图片.png");
	}
}
