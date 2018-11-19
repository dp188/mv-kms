package com.zhixin.core.entities.query;

import java.io.Serializable;

import com.zhixin.core.enums.QueryOperatorEnum;

/**
 * 
 * @ClassName: Condition
 * @Description: 查询条件定义
 * @author zhangtiebin@bwcmall.com
 * @date 2015年7月1日 下午10:47:27
 *
 */
public class Condition implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 2253154831306625515L;
	// 查询字段名称
	private String name;
	// 查询算子
	private QueryOperatorEnum op;
	// 查询值
	private Object value;
	//查询字段名称
	private String colName;
 
	public Condition(){
		
	}
	
	public Condition(String name, QueryOperatorEnum op, Object value,
			String colName) {
		super();
		this.name = name;
		this.op = op;
		this.value = value;
		this.colName = colName;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public QueryOperatorEnum getOp() {
		return op;
	}

	public void setOp(QueryOperatorEnum op) {
		this.op = op;
	}

	public Object getValue() {
		return value;
	}

	public void setValue(Object value) {
		this.value = value;
	}

	public String getColName() {
		return colName;
	}

	public void setColName(String colName) {
		this.colName = colName;
	}

	public String getDbOp() {
		return op.getDbOp();
	} 

}
