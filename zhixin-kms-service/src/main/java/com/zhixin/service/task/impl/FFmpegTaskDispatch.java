package com.zhixin.service.task.impl;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.zhixin.core.utils.SpringContextUtil;
import com.zhixin.dto.task.FMPTaskDTO;
import com.zhixin.service.task.IFMPTaskService;

/**
 * 视频合并等任务处理调度
 * 
 * @author zhangtiebin@bwcmall.com
 * @description
 * @class FFmpegTaskDispatch
 * @package com.zhixin.service.task
 * @Date 2016年1月16日 下午8:30:26
 */

public class FFmpegTaskDispatch {

	private static ExecutorService pool = null;
	
	private static String ffmpegInstallPath;
	
	public static void init(int maxTaskSize,String ffmpegInstallPath) {
		pool = Executors.newFixedThreadPool(maxTaskSize); 
		FFmpegTaskDispatch.ffmpegInstallPath = ffmpegInstallPath; 
	}

	/**
	 * 关闭线程池
	 */
	public static void destory() {
		if (pool != null) {
			if (!pool.isShutdown()) {
				pool.shutdown();
			}
		}
	}

	public static void addTask(String name, String filePath) {
		IFMPTaskService fmpTaskService = (IFMPTaskService) SpringContextUtil.getBean("FMPTaskServiceImpl");
		FMPTaskDTO dto = new FMPTaskDTO();
		dto.setCount(0);
		dto.setFilePath(filePath);
		dto.setTaskName(name);
		Long id = fmpTaskService.create(dto);
		dto.setId(id);

		addTask(dto);

	}

	/**
	 * 添加文件
	 * 
	 * @param task
	 */
	public static void addTask(FMPTaskDTO task) {
		FMPRunner runner = new FMPRunner(task,ffmpegInstallPath);
		pool.submit(runner);

	}

}
