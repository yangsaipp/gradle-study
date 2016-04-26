package com.comtop.cap.component.loader;

import java.io.InputStream;
import java.io.OutputStream;

import com.comtop.cap.component.loader.config.LoaderConfig;
import com.comtop.cap.component.loader.exception.LoadException;

/**
 * 上传下载超级接口,定义了上传下载的方法<br>
 * 所有实现了该接口的类都必须实现文件上传下载删除等功能
 * @author sai.yang
 */
public interface Loadable {
	
	/**
	 * 用于配置loader的相关参数,若loader没有相关参数,可以不理会
	 * @param config 带有loader的相关配置信息
	 */
	void configure(LoaderConfig config);
	
	/**
	 * 打开一个服务器连接,用来进行上传和下载.<br>
	 * 主要用于批量上传和下载
	 */
	void openServer();
	
	/**
	 * 上传 ,该方法会调用openServer()方法打开一个连接 在下载完后会关闭连接<br>
	 * 注意：该方法的实现有义务在上传完(不管成功还是失败)后将inputStream流关闭
	 * @param inputStream	上传资源的IO
	 * @param folderPath	上传后文件存放的文件夹路径
	 * @param fileName		上传后文件存放的名称
	 * @exception LoadException 当上传失败的时候会抛出<code>LoadException</code>
	 */
	void upload(InputStream inputStream, String folderPath, String fileName);
	
	/**
	 * 	下载,该方法会调用openServer()方法打开一个连接 在下载完后会关闭连接<br>
	 * 注意：该方法的实现有义务在上传完(不管成功还是失败)后将outputStream流关闭
	 * @param outputStream  下载资源的IO
	 * @param folderPath	下载的文件所在的文件夹路径
	 * @param fileName	            要下载的文件名称
	 * @exception LoadException	当获取InputStream失败的时候会抛出<code>LoadException</code>
	 */
	void downLoad(OutputStream outputStream, String folderPath, String fileName);
	
	/**
	 * 删除服务器上的资源
	 * @param folderPath		要删除的文件的路径
	 * @param fileName			要删除的文件的名称
	 * @exception LoadException 当删除失败的时候会抛出<code>LoadException</code>
	 */
	void delete(String folderPath, String fileName);
	
	/**
	 * 批量上传所使用的方法,该方法不会自己调用openServer()打开连接,完成之后也不会关闭连接<br>
	 * 所以这里需要调用者自己调用openServer方法打开连接，在调用此方法进行上传,等所有上传完后需要自己调用closeServer关闭
	 * @param inputStream 上传资源的IO
	 * @param folderPath  上传后文件存放的文件夹路径
	 * @param fileName    上传后文件存放的名称
	 */
	void batchUpload(InputStream inputStream, String folderPath, String fileName);
	
	/**
	 * 批量下载所使用的方法,该方法不会自己调用openServer()打开连接,完成之后也不会关闭连接<br>
	 * 所以这里需要调用者自己调用openServer方法打开连接，在调用此方法进行上传,等所有下载完后需要自己调用closeServer关闭
	 * @param outputStream  下载资源的IO
	 * @param folderPath	下载的文件所在的文件夹路径
	 * @param fileName	            要下载的文件名称
	 */
	void batchDownLoad(OutputStream outputStream, String folderPath, String fileName);
	
	/**
	 * 批量删除所使用的方法,该方法不会自己调用openServer()打开连接,完成之后也不会关闭连接<br>
	 * 所以这里需要调用者自己调用openServer方法打开连接，在调用此方法进行上传,等所有删除完后需要自己调用closeServer关闭
	 * @param folderPath	文件路径
	 * @param fileName		文件名
	 */
	void batchDelete(String folderPath, String fileName);
	
	/**
	 * 将保存的服务器连接关闭掉
	 */
	void closeServer();
	
	/**
	 * 
	 * @Methodname: getFileNamesFromFolder
	 * @Discription: 获取文件夹下所有文件名
	 * @param folderPath	路径
	 * @return 文件夹下的所有文件名
	 */
	String[] getFileNamesFromFolder(String folderPath);
}
