package com.zhixin.core.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * 
 * @ClassName: FileUtil
 * @Description: 文件工具类
 * @date 2015年7月15日
 *
 */
public class FileUtil {
	
	/**
	 * 读取文件
	 * @param file path
	 * @return
	 */
	public static String readFile(String path){
		BufferedReader reader = null;
        try{
        	InputStream in = FileUtil.class.getResourceAsStream(path);
        	InputStreamReader inputStreamReader = new InputStreamReader(in, "UTF-8");
        	reader = new BufferedReader(inputStreamReader);
        	String str = reader.readLine();
        	StringBuffer sb = new StringBuffer();
        	while(str != null){
        		sb.append(str);
        		str = reader.readLine();
        	}
        	reader.close();
        	return sb.toString();
        }catch(Exception e){
        	e.printStackTrace();
        }finally{
        	if(reader != null){
        		try {
        			reader.close();
        		} catch (IOException e) {
        			e.printStackTrace();
        		}
        	}
        }
        return "";
	}

	public static String readAbsolutelyFile(String filePath){
		BufferedReader reader = null;
        try{
        	InputStream in = new FileInputStream(new File(filePath));
        	InputStreamReader inputStreamReader = new InputStreamReader(in, "UTF-8");
        	reader = new BufferedReader(inputStreamReader);
        	String str = reader.readLine();
        	StringBuffer sb = new StringBuffer();
        	while(str != null){
        		sb.append(str);
        		str = reader.readLine();
        	}
        	reader.close();
        	return sb.toString();
        }catch(Exception e){
        	e.printStackTrace();
        }finally{
        	if(reader != null){
        		try {
        			reader.close();
        		} catch (IOException e) {
        			e.printStackTrace();
        		}
        	}
        }
        return "";
	}
	
   
}
