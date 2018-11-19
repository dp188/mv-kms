package com.zhixin.core.enums;
/**
 * 
* @ClassName: QueryOperatorEnum 
* @Description: RESTFul 查询操作算子定义
* @author zhangtiebin@bwcmall.com
* @date 2015年7月1日 下午10:41:40 
*
 */
public enum QueryOperatorEnum {
	none("none","none","none") //表示不支持所有操作
	,all("all","all","all") //表示支持所有操作
	,lt("lt","<","小于")
	,lte("lte","<=","小于等于")
	,gt("gt",">","大于等于")
	,gte("gte",">=","大于等于")
	,eq("eq","=","等于")
	,ne("ne","!=","不等于")
	,in("in","in","在xx之间") 
	,contains("contains","like","模糊匹配") 
	,containsList("containsList","containsList","模糊匹配");
	private String op;
	private String desc;
	private String dbOp;
	private QueryOperatorEnum(String op, String dbOp, String desc) {
		this.op = op;
		this.desc = desc;
		this.dbOp = dbOp;
	}
	public String getOp() {
		return op;
	}
	public void setOp(String op) {
		this.op = op;
	}
	public String getDesc() {
		return desc;
	}
	public void setDesc(String desc) {
		this.desc = desc;
	}
	public String getDbOp() {
		return dbOp;
	}
	public void setDbOp(String dbOp) {
		this.dbOp = dbOp;
	}
	
}
