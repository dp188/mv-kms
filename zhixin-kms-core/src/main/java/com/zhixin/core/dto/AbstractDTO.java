package com.zhixin.core.dto;

import com.zhixin.core.annotation.QueryConfig;
import com.zhixin.core.enums.QueryOperatorEnum;

/**
 * 
 * @ClassName: AbstractDomain
 * @Description: entity抽象类
 * @author zhangtiebin@bwcmall.com
 * @date 2015年6月24日 下午3:31:39
 * 
 */
public abstract class AbstractDTO {

	//创建人
	@QueryConfig(colname = "created_by", supportOrder = true, supportOps = { QueryOperatorEnum.eq,
			QueryOperatorEnum.in })
	private Long createdBy;
	//创建人
	@QueryConfig(colname = "created_by", supportOrder = true, supportOps = { QueryOperatorEnum.eq,
			QueryOperatorEnum.in })
	private String createdByName;
	//更新人
	@QueryConfig(colname = "updated_by", supportOrder = true, supportOps = { QueryOperatorEnum.eq,
			QueryOperatorEnum.in })
	private Long updatedBy;
	//更新人
	@QueryConfig(colname = "updated_by", supportOrder = true, supportOps = { QueryOperatorEnum.eq,
			QueryOperatorEnum.in })
	private String updatedByName;
	//创建时间
	@QueryConfig(colname = "created_time", supportOrder = true, supportOps = { QueryOperatorEnum.eq,
			QueryOperatorEnum.gt, QueryOperatorEnum.gte, QueryOperatorEnum.lt, QueryOperatorEnum.lte })
	private String createdTime;
	//修改时间
	@QueryConfig(colname = "updated_time", supportOrder = true, supportOps = { QueryOperatorEnum.eq,
			QueryOperatorEnum.gt, QueryOperatorEnum.gte, QueryOperatorEnum.lt, QueryOperatorEnum.lte })
	private String updatedTime;
	//修改时间
	@QueryConfig(colname = "status", supportOrder = true, supportOps = { QueryOperatorEnum.eq })
	//0 deleted 1 activied 
	private Integer status;
	//排序号
	private Integer sn;

	//
	private String dataOperation;

	public String getDataOperation() {
		return dataOperation;
	}

	public void setDataOperation(String dataOperation) {
		this.dataOperation = dataOperation;
	}

	public String getCreatedTime() {
		return createdTime;
	}

	public void setCreatedTime(String createdTime) {
		this.createdTime = createdTime;
	}

	public String getUpdatedTime() {
		return updatedTime;
	}

	public void setUpdatedTime(String updatedTime) {
		this.updatedTime = updatedTime;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Integer getSn() {
		return sn;
	}

	public void setSn(Integer sn) {
		this.sn = sn;
	}

	public Long getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(Long createdBy) {
		this.createdBy = createdBy;
	}

	public Long getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(Long updatedBy) {
		this.updatedBy = updatedBy;
	}

	public String getCreatedByName() {
		return createdByName;
	}

	public void setCreatedByName(String createdByName) {
		this.createdByName = createdByName;
	}

	public String getUpdatedByName() {
		return updatedByName;
	}

	public void setUpdatedByName(String updatedByName) {
		this.updatedByName = updatedByName;
	}

	public abstract Object getPK();
}
