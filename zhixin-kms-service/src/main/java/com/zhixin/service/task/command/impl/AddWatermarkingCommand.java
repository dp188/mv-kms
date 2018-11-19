package com.zhixin.service.task.command.impl;

import java.io.File;
import java.util.UUID;

import com.zhixin.service.task.command.FFmpegHelper;
import com.zhixin.service.task.command.FMPCommand;
/**
 * 图片加水印命令
 * @author zhangtiebin@bwcmall.com
 * @description  
 * @class AddWatermarkingCommand
 * @package com.zhixin.service.task.command.impl
 * @Date 2016年1月16日 下午11:04:31
 */
public class AddWatermarkingCommand extends FMPCommand {
	// 视频文件路径
	private File vedioFile;
	// 水印图片路径
	private File waterMarkingFile;

	public AddWatermarkingCommand(File vedioFile, File waterMarkingFile) {
		super();
		this.vedioFile = vedioFile;
		this.waterMarkingFile = waterMarkingFile;
	}

	@Override
	public String execute() {
		StringBuffer command = new StringBuffer();
		String outfile = vedioFile.getParentFile().getAbsolutePath()+File.separator+UUID.randomUUID().toString()+".mp4";
		command.append(getFfmpegInstallPath())
		.append(" -i ").append( vedioFile.getAbsolutePath())
		.append(" -i "+waterMarkingFile.getAbsolutePath())
		.append(" -filter_complex \"overlay=5:5\" -y ")
		.append(outfile);
		//执行命令
		 FFmpegHelper.executeFFmpegCommand(command.toString());
		 //删除以前的文件
		 vedioFile.delete();
		 //重命名到以前的文件
		 new File(outfile).renameTo(vedioFile);
		return vedioFile.getAbsolutePath();
	}

}
