package com.zhixin.core.entities.query;

import java.io.Serializable;

/**
 * RESTFul 分页元数据信息
 * @author Administrator
 *
 */
public class PagerResultInfo implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 8148088945672856945L;
	//数据库中符合GET条件的记录总数
	private Integer total_count;
	//每页的个数
	private Integer per_page;
	//当前页数
	private Integer page;
	//总页数
	private Integer page_count;
	
	
	public Integer getTotal_count() {
		return total_count;
	}
	public void setTotal_count(Integer total_count) {
		this.total_count = total_count;
	}
	public Integer getPer_page() {
		return per_page;
	}
	public void setPer_page(Integer per_page) {
		this.per_page = per_page;
	}
	public Integer getPage() {
		return page;
	}
	public void setPage(Integer page) {
		this.page = page;
	}
	public Integer getPage_count() {
		return page_count;
	}
	public void setPage_count(Integer page_count) {
		this.page_count = page_count;
	}
	
}
