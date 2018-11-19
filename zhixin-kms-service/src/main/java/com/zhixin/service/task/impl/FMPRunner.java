package com.zhixin.service.task.impl;

import java.awt.Font;
import java.io.File;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.zhixin.core.common.exceptions.ApiException;
import com.zhixin.core.enums.ErrorCodeEnum;
import com.zhixin.core.enums.TaskStatusEnum;
import com.zhixin.core.utils.PNGUtil;
import com.zhixin.core.utils.SpringContextUtil;
import com.zhixin.dto.task.FMPTaskDTO;
import com.zhixin.service.task.IFMPTaskService;
import com.zhixin.service.task.command.FMPCommand;
import com.zhixin.service.task.command.impl.AddSubtitlesCommand;
import com.zhixin.service.task.command.impl.FileBackupCommand;
import com.zhixin.service.task.command.impl.MergeVedioCommand;

/**
 * task
 * 
 * @author zhangtiebin@bwcmall.com
 * @description
 * @class FMPRunner
 * @package com.zhixin.service.task.impl
 * @Date 2016年1月16日 下午9:24:27
 */
public class FMPRunner implements Runnable {
	
	private final Logger log = LoggerFactory.getLogger(FMPRunner.class);
	private String ffmpegInstallPath;
	private FMPTaskDTO task;

	public FMPRunner(FMPTaskDTO task,String ffmpegInstallPath) {
		super();
		this.task = task;
		this.ffmpegInstallPath = ffmpegInstallPath;
	}

	@Override
	public void run() {
		String titlefilepath  = task.getFilePath()+File.separator+"/title.png";
		IFMPTaskService fmpTaskService = (IFMPTaskService)SpringContextUtil.getBean("FMPTaskServiceImpl");
		try {
			log.info("开启视频任务：taskId={},taskName={},当前是第{}次",task.getId(),task.getTaskName(),task.getCount());
			//更新任务为执行中，并增加执行次数
			task.setTaskStatus(TaskStatusEnum.inprogress.getValue()); 
			if(task.getCount() == null){
				task.setCount(task.getCount()+1);
			}else{
				task.setCount(task.getCount()+1);
			}
			//更新任务执行状况
			fmpTaskService.update(task);
			//生成图片
			log.info("生成水印图片，图片地址为：{}",titlefilepath);
			PNGUtil.createImage("中华人民共和国", new Font("宋体", Font.BOLD, 18), new File(titlefilepath));
			log.info("生成水印图片成功，图片地址为：{}",titlefilepath);
			
			//执行文件备份指令
			FMPCommand command = new FileBackupCommand(task.getFilePath());
			command.setFfmpegInstallPath(ffmpegInstallPath);
			 command.execute();
			//合并视频
			String vedioFile = mergeVedioFiles(task.getFilePath());
			//查找到字幕文件
			String subtitlesFile = getSubtitlesFile(task.getFilePath()); //算法，得到一个字幕文件
			//添加字幕文件
			vedioFile = addSubtitles(vedioFile,subtitlesFile);
			//视频加入水印
			vedioFile = addWatermarking(vedioFile,titlefilepath);
			log.info("完成视频任务：taskId={},taskName={},当前是第{}次",task.getId(),task.getTaskName(),task.getCount());
		} catch (Exception e) {
			e.printStackTrace(); 
			log.error(e.getMessage(),e);
			task.setTaskStatus(TaskStatusEnum.faild.getValue()); 
			//更新任务执行状况
			fmpTaskService.update(task);
			log.error("处理视频任务失败：taskId={},taskName={},当前是第{}次",task.getId(),task.getTaskName(),task.getCount());
		}finally{
			
		}
	}
	/**
	 * 通过规则找到我们的字幕文件
	 * @param filepth
	 * @return
	 */
	private String getSubtitlesFile(String fileDirs){
		File file = new File(fileDirs);
	
		String[] subFileArray = file.list();
		
		if(subFileArray != null && subFileArray.length>0){
			for(String subfile : subFileArray){
				if(subfile.endsWith(".txt")){
					return subfile;
				}
			}
		} else{
			throw new ApiException(ErrorCodeEnum.SystemError.getCode(),"不存在字幕文件！");
		}
		return null;
	}
	/**
	 * 合并视频
	 * @param filepth
	 * @return
	 */
	private String mergeVedioFiles(String filepth){

		FMPCommand command = new MergeVedioCommand(filepth);
		
		command.setFfmpegInstallPath(ffmpegInstallPath);
		
		return command.execute();
	}
	/**
	 * 添加字幕文件
	 * @param vedioFile
	 * @param subtitlesFile
	 * @return
	 */
	private String addSubtitles(String vedioFile,String subtitlesFile){
		
		FMPCommand command = new AddSubtitlesCommand(new File(vedioFile),new File(subtitlesFile));
		
		command.setFfmpegInstallPath(ffmpegInstallPath);
		
		return command.execute();
	}
	/**
	 * 添加水印
	 * @param vedioFile
	 * @param pngfile
	 * @return
	 */
	private String addWatermarking(String vedioFile, String subtitlesFile){
		FMPCommand command = new AddSubtitlesCommand(new File(vedioFile),new File(subtitlesFile));
		command.setFfmpegInstallPath(ffmpegInstallPath);
		return command.execute();
	} 
	 
}
