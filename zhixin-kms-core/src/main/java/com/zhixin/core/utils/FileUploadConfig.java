package com.zhixin.core.utils;

import java.io.IOException;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Properties;
import org.apache.log4j.Logger;
/****
 * 文件上传配置信息
 * @author Administrator
 *
 */
public class FileUploadConfig {
	
	protected static final Logger logger = Logger.getLogger(FileUploadConfig.class);
	
	//###########文件上传配置
	//FileUploadPath: 文件上传文件存放跟根目录
	private static String  file_upload_path="d:\\mutifiles\\";

	//FileFormat:文件上传后重新命名格式 ${fileName} 文件原始名称,${type} 文件类型, ${uuid} 统一地址
	private static String file_format="${uuid}${fileName}${type}";

	//File_size_limit:单个文件的最的值
    private static String file_size_limit="2048 MB";

	//File_upload_limit:每一个计量可以上传附件的最多个数
    private static String 	file_upload_limit="7";

	//File_types:可上传文件类型列表
    private static String file_types="*.zip;*.rar;*.png;*.doc;*.docx;*.ppt;*.pptx;*.xls";

	//File_queue_limit:可以列表数,即每次待上传列队可以有多少个文件.0表示无限制
    private static String file_queue_limit="0";
    
    private static Properties ps=new Properties();
	
    //private static Object lock=new Object();
    
    static{
    	try {
			ps.load(FileUploadConfig.class.getClassLoader().getResourceAsStream("file_upload_config.properties"));
			if(ps.get("file_upload_path")!=null){
				file_upload_path=(String) ps.get("file_upload_path");
			}
			if(ps.get("file_format")!=null){
				file_format=(String) ps.get("file_format");
			}
			if(ps.get("file_size_limit")!=null){
				file_size_limit=(String) ps.get("file_size_limit");
			}
			if(ps.get("file_upload_limit")!=null){
				file_upload_limit=(String) ps.get("file_upload_limit");
			}
			if(ps.get("file_types")!=null){
				file_types=(String) ps.get("file_types");
			}
			if(ps.get("file_queue_limit")!=null){
				file_queue_limit=(String) ps.get("file_queue_limit");
			}
    	} catch (IOException e) {
			e.printStackTrace();
		}   	
    }
    
	
	/**
	 * 获取文件上传根路径
	 * @return
	 */
	public static String getFileUploadPath(String str){
		if(str == null || "".equals(str) || "null".equals(str)){
			return analysisParameter(file_upload_path);		
		}else{
			if(ps.get(str)!=null){
				return analysisParameter((String)ps.get(str));		
			}else{
				logger.debug(str+"对应路径不存在!");
			}
			return null;
		}
	}
	
	public static String getFileFormat(){
		return file_format;
	}
	
	
	
//  可设置允许上传的文件大小(File_size_limit),如果设置为100则表示100MB，最大支持2G
//	 (注意：此控件并非完全解决大文件上传，建议上传小于50MB的文件,
//	  如需上传大文件,建议了解分块上传)
	/****
	 * 文件大小
	 * @param map
	 * @return
	 */
	public static String  getFile_size_limit(){
		return file_size_limit;		
	}



	
	
	/*****
	 * 可上传文件个数
	 * @param map
	 * @return
	 */
	public static String getFile_upload_limit(){
		return file_upload_limit;
	}
	
	/***
	 * 可上传的文件类型
	 * @param map
	 * @return
	 */
	public static String getFile_types(){
		return file_types;		
	}

	
	/***
	 * 获取文件名
	 * @param uuid
	 * @param fileName
	 * @return
	 */
	public static String getFileName(String uuid,String fileName){
		return analysisParameter(getFileFormat()).replace("${uuid}", uuid).replace("${fileName}", fileName.subSequence(0, fileName.lastIndexOf("."))).replace("${type}",fileName.substring(
						fileName.lastIndexOf("."),
						fileName.length()));
	}
	
	
	/***
	 * 每次可列队的文件个数 0无限制
	 * @param map
	 * @return
	 */
	public static String getFile_queue_limit(){
		return file_queue_limit;		
	}
	
	
	/****
	 * 解析变量
	 * @param source
	 * @return
	 */	
	private static String analysisParameter(String source){
		if(source==null) return "";
		Calendar calendar= new GregorianCalendar();
		return source.replace("${year}", ""+calendar.get(Calendar.YEAR))
			.replace("${month}", ""+(calendar.get(Calendar.MONTH)+1))
			.replace("${day}", ""+calendar.get(Calendar.DAY_OF_MONTH))
			.replace("${hour}", ""+calendar.get(Calendar.HOUR_OF_DAY))
			.replace("${minute}", ""+calendar.get(Calendar.MINUTE))
			.replace("${second}", ""+calendar.get(Calendar.SECOND));
	}
	
	/*public static void main(String[] args) {	
		Calendar calendar= new GregorianCalendar();
		System.out.println(calendar.get(Calendar.YEAR));
		System.out.println(calendar.get(Calendar.MONTH)+1);
		System.out.println(calendar.get(Calendar.DAY_OF_MONTH));
		
		System.out.println(calendar.get(Calendar.MINUTE));
		System.out.println(calendar.get(Calendar.SECOND));
		String data="${year}-${month}-${day} ${hour}:${minute}:${second}";
		
		System.out.println(analysisParameter(data));
	}*/
}
