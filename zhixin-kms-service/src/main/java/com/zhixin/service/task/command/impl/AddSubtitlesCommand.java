package com.zhixin.service.task.command.impl;

import java.io.File;
import java.util.UUID;

import com.zhixin.service.task.command.FFmpegHelper;
import com.zhixin.service.task.command.FMPCommand;
/**
 * 添加字幕
 * @author zhangtiebin@bwcmall.com
 * @description  
 * @class AddSubtitlesCommand
 * @package com.zhixin.service.task.command.impl
 * @Date 2016年1月16日 下午11:09:32
 */
public class AddSubtitlesCommand extends FMPCommand {
	// 视频文件路径
	private File vedioFile;
	// 字幕文件路径
	private File subtitlesFile;
	
	public AddSubtitlesCommand(File vedioFile, File subtitlesFile) {
		super();
		this.vedioFile = vedioFile;
		this.subtitlesFile = subtitlesFile;
	}


	@Override
	public String execute() {
		StringBuffer command = new StringBuffer();
		String outfile = vedioFile.getParentFile().getAbsolutePath()+File.separator+UUID.randomUUID().toString()+".mp4";
		command.append(getFfmpegInstallPath())
		.append(" -i ").append( vedioFile.getAbsolutePath())
		.append(" -vf subtitles="+subtitlesFile.getAbsolutePath())
		.append(" -y ")
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
