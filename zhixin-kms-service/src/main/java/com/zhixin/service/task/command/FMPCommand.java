package com.zhixin.service.task.command;

import com.zhixin.core.common.exceptions.ApiException;

/**
 * FMP命令根类
 * 
 * @author zhangtiebin@bwcmall.com
 * @description
 * @class FMPCommand
 * @package com.zhixin.service.task.command
 * @Date 2016年1月16日 下午10:46:20
 */
public abstract class FMPCommand {
	// ffmpeg文件路径，即命令路径
	private String ffmpegInstallPath;

	public String getFfmpegInstallPath() {
		return ffmpegInstallPath;
	}

	public void setFfmpegInstallPath(String ffmpegInstallPath) {
		this.ffmpegInstallPath = ffmpegInstallPath;
	}

	public abstract String execute() throws ApiException;

}
