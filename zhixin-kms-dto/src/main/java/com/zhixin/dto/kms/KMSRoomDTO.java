package com.zhixin.dto.kms;

import java.io.Serializable;
import java.util.Date;

import com.zhixin.core.dto.AbstractDTO;

//视频通话房间
public class KMSRoomDTO extends AbstractDTO implements Serializable {
	private static final long serialVersionUID = 1L;
	//
	private Long id;
	//
	private String name;
	//
	private String key;
	//
	private Date videoStartTime;
	// false半双工，true双工
	private String isDuplexing;
	// 备注
	private String remark;
	// 文件存储路径
	private String livePath;
	
	private Integer userCount;

	public void setId(Long id) {
		this.id = id;
	}

	public Long getId() {
		return this.id;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return this.name;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getKey() {
		return this.key;
	}

	public void setVideoStartTime(Date videoStartTime) {
		this.videoStartTime = videoStartTime;
	}

	public Date getVideoStartTime() {
		return this.videoStartTime;
	}

	public void setIsDuplexing(String isDuplexing) {
		this.isDuplexing = isDuplexing;
	}

	public String getIsDuplexing() {
		return this.isDuplexing;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getRemark() {
		return this.remark;
	}

	public void setLivePath(String livePath) {
		this.livePath = livePath;
	}

	public String getLivePath() {
		return this.livePath;
	}

	@Override
	public Long getPK() {
		return id;
	}

	public Integer getUserCount() {
		return userCount;
	}

	public void setUserCount(Integer userCount) {
		this.userCount = userCount;
	}

	 
}
