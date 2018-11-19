package com.zhixin.core.utils;

import org.springframework.util.StringUtils;

/**
 * 
 * 
 * @ClassName:  CodeSplitUtil
 * @Description: code编辑工具
 * @author zhangtb@bwcmall.com
 * @date 2015年10月10日 下午3:00:55
 *
 */
public class CodeSplitUtil {
	/**
	 * 按length切割成带.的字符串，如果00010002按4会切割成0001.0002
	 * @param parentCode
	 * @param code
	 * @return
	 */
	public static String splitCode(String parentCode,String code){
		int length = 0;
		if(StringUtils.isEmpty(parentCode)){
			length =  code.length();
		}else{
			length =  code.length()-parentCode.length();
		}
		return splitCode(code,length);
	}
	/**
	 * 按length切割成带.的字符串，如果00010002按4会切割成0001.0002
	 * @param code
	 * @param length
	 * @return
	 */
	public static String splitCode(String code,int length){
		 StringBuilder sb = new StringBuilder();
		 int i = 0;
		 while(i*length<code.length()){
			 if(sb.length() == 0){
				 sb.append(code.substring(i*length, (i+1)*length));
			 }else{
				 sb.append(".").append(code.substring(i*length, (i+1)*length));
			 } 
			 i = i+1;
		 } 
		return sb.toString();
	}
}
