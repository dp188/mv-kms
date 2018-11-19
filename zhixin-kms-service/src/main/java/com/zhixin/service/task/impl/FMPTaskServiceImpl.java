package com.zhixin.service.task.impl;

import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.zhixin.dao.task.IFMPTaskDao;
import com.zhixin.dto.task.FMPTaskDTO;
import com.zhixin.entities.task.FMPTaskEntity;
import com.zhixin.service.base.impl.AbstractBaseService;
import com.zhixin.service.task.IFMPTaskService;
 
@Service("FMPTaskServiceImpl")
public class FMPTaskServiceImpl extends 	AbstractBaseService<FMPTaskDTO,FMPTaskEntity, Long> implements IFMPTaskService ,InitializingBean,DisposableBean{
	/**
	 * 队列中最大线程数量
	 */
	@Value("${maxTaskSize}")
	private int maxTaskSize;
	
	@Value("${ffmpegInstallPath}")
	private String ffmpegInstallPath;
	
	@ Autowired
	public FMPTaskServiceImpl(IFMPTaskDao fMPTaskDao ) {
		super(fMPTaskDao);
	}
	/**
	 * 启动的时候，初始化线程池
	 */
	@Override
	public void afterPropertiesSet() throws Exception {
		FFmpegTaskDispatch.init(maxTaskSize,ffmpegInstallPath);
		
	}
	/**
	 * 关闭的时候，关闭线程池
	 */
	@Override
	public void destroy() throws Exception {
		FFmpegTaskDispatch.destory();
	} 
}
