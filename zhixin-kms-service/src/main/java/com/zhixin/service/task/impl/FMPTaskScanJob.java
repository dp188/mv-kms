package com.zhixin.service.task.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.zhixin.core.common.pager.Pager;
import com.zhixin.core.entities.query.QueryCondition;
import com.zhixin.core.enums.QueryOperatorEnum;
import com.zhixin.core.enums.TaskStatusEnum;
import com.zhixin.core.utils.SpringContextUtil;
import com.zhixin.dto.task.FMPTaskDTO;
import com.zhixin.service.task.IFMPTaskService;

/**
 *  fmp定时扫描工具类，扫描失败任务，用来重启视频任务
 * @author zhangtiebin@bwcmall.com
 * @description  
 * @class FMPTaskScanJob
 * @package com.zhixin.service.task.impl
 * @Date 2016年1月16日 下午8:56:50
 */
@Component("scanJob")
public class FMPTaskScanJob {

	/**
	 * 最大重试次数
	 */
	@Value("${maxRetryTimes}")
	private int maxRetryTimes;
	
	public void run(){
		IFMPTaskService fmpTaskService = (IFMPTaskService)SpringContextUtil.getBean("FMPTaskServiceImpl");
		if(fmpTaskService != null){
			QueryCondition queryCondition = new QueryCondition();
			List<Integer> statusList = new ArrayList<Integer>();
			statusList.add(0);
			statusList.add(3); 
			queryCondition.setPage(0);
			queryCondition.setPer_page(100);
			queryCondition.addCondition("count", "count", QueryOperatorEnum.lt, maxRetryTimes);
			queryCondition.addCondition("taskStatus", "task_status", QueryOperatorEnum.eq, TaskStatusEnum.faild);
			
			Pager<FMPTaskDTO> pager = fmpTaskService.findForPager(queryCondition);
			while(pager != null && pager.getPageItems() != null && pager.getPageItems().size()>0){
				List<FMPTaskDTO> list = pager.getPageItems();
				for(FMPTaskDTO dto : list){
					FFmpegTaskDispatch.addTask( dto);
				}
				queryCondition.setPage(pager.getCurrPage());
				queryCondition.setPer_page(pager.getPageSize());
				queryCondition.addCondition("count", "count", QueryOperatorEnum.lt, maxRetryTimes);
				queryCondition.addCondition("taskStatus", "task_status", QueryOperatorEnum.eq, TaskStatusEnum.faild);
				
				pager = fmpTaskService.findForPager(queryCondition);
			}
		}
	}
}
