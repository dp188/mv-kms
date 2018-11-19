package com.zhixin.core.common.pager;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tonymac on 15/2/2.
 */
public class Pager<E> {

    
	/**返回当前页号 **/
    private int currPage;

    /**返回分页大小 **/
    private int pageSize; 

    /**返回总页数 **/
    private int pageCount; 

    /**返回当前页的记录条数 **/
    private int pageRowsCount;

    /**返回记录总行数 **/
    private int rowsCount;
	
	/**返回记录详细内容 **/
	private List<E> pageItems = new ArrayList<>();

    public Pager() {
    }

    public Pager(int curPage, int pageSize, int rowsCount, List<E> pageItems) {
        this.currPage = curPage;
        this.pageSize = pageSize;
        this.rowsCount = rowsCount;
        this.pageItems = pageItems;
        int pageCount = (int)Math.ceil((double)rowsCount/(double)pageSize);
        setCurrPage(curPage);
        setPageCount(pageCount);
        setPageItems(pageItems);
        setPageSize(pageSize);
        setRowsCount(rowsCount);
        if(pageItems == null || pageItems.isEmpty() ){
            setPageRowsCount(0);
        }else{
            setPageRowsCount(pageItems.size());
        }
    }

    

    public int getRowsCount() {
		return rowsCount;
	}

	public void setRowsCount(int rowsCount) {
		this.rowsCount = rowsCount;
	}

    public int getCurrPage() {
		return currPage;
	}

	public void setCurrPage(int currPage) {
		this.currPage = currPage;
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public int getPageCount() {
		return pageCount;
	}

	public void setPageCount(int pageCount) {
		this.pageCount = pageCount;
	}

	public int getPageRowsCount() {
		return pageRowsCount;
	}

	public void setPageRowsCount(int pageRowsCount) {
		this.pageRowsCount = pageRowsCount;
	}

	public void setPageItems(List<E> pageItems) {
        this.pageItems = pageItems;
    }

    public List<E> getPageItems() {
        return pageItems;
    }
}
