package com.zhixin.service.task.command.impl;

import java.io.File;
import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.FileCopyUtils;

import com.zhixin.core.common.exceptions.ApiException;
import com.zhixin.core.enums.ErrorCodeEnum;
import com.zhixin.service.task.command.FMPCommand;

/**
 * 文件备份指令
 * @author zhangtiebin@bwcmall.com
 * @description  
 * @class FileBackupCommand
 * @package com.zhixin.service.task.command.impl
 * @Date 2016年1月17日 下午11:47:07
 */
public class FileBackupCommand extends FMPCommand {
	private String workdirs;
	private static final Logger log = LoggerFactory.getLogger(MergeVedioCommand.class);
	
	public FileBackupCommand(String workdirs) {
		super();
		this.workdirs = workdirs;
	}

	@Override
	public String execute() throws ApiException {
		 File backDir = new File(workdirs+File.separator+"back");
		 if(!backDir.exists()){
			 backDir.mkdirs();
		 } 
		 File wokrDirFile = new File(workdirs);
		 
		 try {
			if(wokrDirFile.exists()){
				 File[] workFiles = wokrDirFile.listFiles();
				 File backFile = null;
				 if(workFiles != null && workFiles.length>0){
					 for(File file : workFiles){
						 if(file.isDirectory()){
							 continue;
						 }
						 backFile = new File(backDir.getAbsolutePath()+File.separator+file.getName());
						 if(!backFile.exists()){ //不存在，则备份一次
							 FileCopyUtils.copy(file, backFile);
						 }
					 }
				 }
			 }
		} catch (IOException e) { 
			e.printStackTrace();
			log.error(e.getMessage(),e);
			throw new ApiException(ErrorCodeEnum.SystemError.getCode(),e.getMessage());
		}
		return null;
	}

}
