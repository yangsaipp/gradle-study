package com.comtop.cap.component.loader;

/**
 * loader的类型，增加新的上传下载实现后这里必须要增加相应的类型
 * @Classname: LoaderType
 * @Author: sai.yang
 * @Date: 2012-8-24 下午02:20:56
 * @Version:V6.0
 */
public enum LoaderType {
	/**
	 * http类型,即本地上传
	 */
	 HTTP,
	 /**
	  * ftp类型
	  */
	 FTP;
	 
	 /**
	  * 
	  * @Methodname: isFTP
	  * @Discription: 判断是否是ftp类型
	  * @return true 是ftp false 不是ftp
	  *
	  */
	 public boolean isFTP() {
		 return this.equals(FTP);
	 }
	 
	 /**
	  * 
	  * @Methodname: isHTTP
	  * @Discription: 判断是否是http类型
	  * @return true 是http false 不是http
	  *
	  */
	 public boolean isHTTP() {
		 return this.equals(HTTP);
	 }
}
