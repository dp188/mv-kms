package com.zhixin.core.entities;

import java.sql.Timestamp;

/**
 * 
 * @ClassName: AbstactEntity
 * @Description: domain抽象类
 * @author zhangtiebin@bwcmall.com
 * @date 2015年6月24日 下午3:23:05
 *
 */
public abstract class AbstractEntity<PK> {
	// 创建时间
	private Timestamp createdTime;
	// 创建人
	private Long createdBy;
	// 修改时间
	private Timestamp updatedTime;
	// 修改人
	private Long updatedBy;
	// 0 表示删除 1 未删除
	private Integer status;
	// 排序号
	private Integer sn;
	
	public AbstractEntity() {
		super();
	}

	public AbstractEntity(Timestamp createdTime, Long createdBy,
			Timestamp updatedTime, Long updatedBy, Integer status) {
		super();
		this.createdTime = createdTime;
		this.createdBy = createdBy;
		this.updatedTime = updatedTime;
		this.updatedBy = updatedBy;
		this.status = status;
	}

	public AbstractEntity(Timestamp updatedTime, Long updatedBy) {
		super();
		this.updatedTime = updatedTime;
		this.updatedBy = updatedBy;
	}

	/**
	 * 设置主键
	 * 
	 * @param pk
	 */
	public abstract void setPK(PK pk);

	/**
	 * 获取主键
	 * 
	 * @return
	 */
	public abstract PK getPK();

	public Integer getSn() {
		return sn;
	}

	public void setSn(Integer sn) {
		this.sn = sn;
	}

	public Timestamp getCreatedTime() {
		return createdTime;
	}

	public void setCreatedTime(Timestamp createdTime) {
		this.createdTime = createdTime;
	}

	public Long getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(Long createdBy) {
		this.createdBy = createdBy;
	}

	public Timestamp getUpdatedTime() {
		return updatedTime;
	}

	public void setUpdatedTime(Timestamp updatedTime) {
		this.updatedTime = updatedTime;
	}

	public Long getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(Long updatedBy) {
		this.updatedBy = updatedBy;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}
 

}
