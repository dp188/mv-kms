package com.zhixin.dto.kms;

import java.io.Serializable;

import com.zhixin.core.annotation.QueryConfig;
import com.zhixin.core.dto.AbstractDTO;
import com.zhixin.core.enums.QueryOperatorEnum;

public class KMSMany2ManyDTO extends AbstractDTO implements Serializable {
	private static final long serialVersionUID = 1L;

	private Integer call_type;
	//房间id
	private Long id;
	//房间名字
	@QueryConfig(colname="name",supportOps={QueryOperatorEnum.contains})
	private String name;
	//
	private String key;
	// 备注
	private String remark;
	// 文件存储路径
	private String livePath;
	/**
	 * 用户数
	 */
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
		// TODO Auto-generated method stub
		return id;
	} 

	public Integer getCall_type() {
		return call_type;
	}

	public void setCall_type(Integer call_type) {
		this.call_type = call_type;
	}

	public Integer getUserCount() {
		return userCount;
	}

	public void setUserCount(Integer userCount) {
		this.userCount = userCount;
	}

}
