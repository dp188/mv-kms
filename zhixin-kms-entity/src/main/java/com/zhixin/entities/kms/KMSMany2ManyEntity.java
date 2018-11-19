package com.zhixin.entities.kms;

/**
 * 
 * @ClassName: KMSMany2ManyEntity
 * @Description: 多对多
 * @author tovey1987@126.com
 *	@date 2016年1月3日 
 */
import java.io.Serializable;

import com.alibaba.fastjson.annotation.JSONField;
import com.zhixin.core.entities.AbstractEntity;

//视频多对多通话房间
public class KMSMany2ManyEntity extends AbstractEntity<Long> implements Serializable {
	private static final long serialVersionUID = 1L;// ?
	// 呼叫类型
	private Integer call_type;
	// 房间id
	private Long id;
	// 房间名字
	private String name;
	//
	private String key;
	// 备注
	private String remark;
	// 文件存储路径
	private String livePath;

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

	public Integer getCall_type() {
		return call_type;
	}

	public void setCall_type(Integer call_type) {
		this.call_type = call_type;
	}

	@JSONField(serialize = false)
	@Override
	public void setPK(Long pk) {
		// TODO Auto-generated method stub
		this.id = pk;
	}

	@Override
	public Long getPK() {
		// TODO Auto-generated method stub
		return id;
	}

}
