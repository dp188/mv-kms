package com.zhixin.dto.task;

import java.io.Serializable;
import java.util.Date;

import com.zhixin.core.dto.AbstractDTO;

//视频fmp任务服务
public class FMPTaskDTO extends AbstractDTO implements Serializable {
	private static final long serialVersionUID = 1L;
	// 主键
	private Long id;
	// 任务名称（会议室名称）
	private String taskName;
	// 视频文件以及字幕文件所处的路径
	private String filePath;
	// 任务开始异常
	private Date taskStartDate;
	// 任务的状态：未开始，执行中，执行完成，执行失败
	private Integer taskStatus;
	// 当前是第几次
	private Integer count;

	public void setId(Long id) {
		this.id = id;
	}

	public Long getId() {
		return this.id;
	}

	public void setTaskName(String taskName) {
		this.taskName = taskName;
	}

	public String getTaskName() {
		return this.taskName;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	public String getFilePath() {
		return this.filePath;
	}

	public void setTaskStartDate(Date taskStartDate) {
		this.taskStartDate = taskStartDate;
	}

	public Date getTaskStartDate() {
		return this.taskStartDate;
	}

	public void setTaskStatus(Integer taskStatus) {
		this.taskStatus = taskStatus;
	}

	public Integer getTaskStatus() {
		return this.taskStatus;
	}

	public void setCount(Integer count) {
		this.count = count;
	}

	public Integer getCount() {
		return this.count;
	}

	@Override
	public Long getPK() {
		return id;
	}
}
