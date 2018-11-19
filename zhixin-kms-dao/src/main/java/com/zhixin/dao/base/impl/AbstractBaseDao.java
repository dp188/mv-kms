package com.zhixin.dao.base.impl;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.mybatis.spring.SqlSessionTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.zhixin.core.common.exceptions.ApiException;
import com.zhixin.core.common.pager.Pager;
import com.zhixin.core.entities.AbstractEntity;
import com.zhixin.core.entities.query.QueryCondition;
import com.zhixin.core.enums.StatusEnum;
import com.zhixin.dao.base.IBaseDao;

/**
 * Created by tonymac on 14/12/8.
 */
public abstract class AbstractBaseDao<T, PK extends Serializable> implements IBaseDao<T, PK> {
	protected Logger logger = LoggerFactory.getLogger(this.getClass());
	protected String packageName;
	
	public AbstractBaseDao(String packageName) {
		this.packageName = packageName;
	}

	/**
	 * 从子类获取SqlSessionTemplate，用来支持多数据源
	 * 
	 * @return
	 */
	protected abstract SqlSessionTemplate getSqlSession();

	@SuppressWarnings("unchecked")
	@Override
	public PK create(T entity) {
		getSqlSession().insert(packageName + ".create", entity);
		return ((AbstractEntity<PK>) entity).getPK();
	}

	@Override
	public List<T> findAll() {
		return getSqlSession().selectList(packageName + ".findAll");
	}

	@Override
	public int update(T entity) {
		return getSqlSession().update(packageName + ".update", entity);
	}
	/**
	 * 根据主键更新状态
	 * @param id
	 * @param status
	 */
	public void updateStatus(Long id, StatusEnum status){
		Map<String,Object> parameter  = new HashMap<String,Object>();
		parameter.put("id", id);
		parameter.put("status", status.ordinal());
		 getSqlSession().update(packageName + ".updateStatus", parameter);
	}
	@Override
	public int remove(PK pk) {
		return  getSqlSession().delete(packageName + ".remove", pk);
	}

	@Override
	public T find(PK pk) {
		return getSqlSession().selectOne(packageName + ".find", pk);
	}
	@Override
	public List<T> find(List<PK> pkList) {
		return getSqlSession().selectList(packageName + ".findByPKList", pkList);
	}
	@Override
	public List<T> findByBeanProp(T entity) {
		return getSqlSession().selectList(packageName + ".findByBeanProp",
				entity);
	}

	@SuppressWarnings("unchecked")
	@Override
	public Pager<T> findForPager(T entity, Integer curPage, Integer pageSize) {
		Map<String,Object> parameter = (Map<String,Object>) JSON.toJSON(entity);
		if (curPage == 0){
			curPage = 1;
		}
			
		if (pageSize == 0){
			pageSize = 20;
		}

		return findForPager("findForPager", parameter, curPage, pageSize);
	}
	@SuppressWarnings("unchecked")
	@Override
	public Pager<T> findForPager(QueryCondition queryCondition)
			throws ApiException {
		Map<String,Object> parameter = (Map<String,Object>) JSON.toJSON(queryCondition);
		int curPage = queryCondition.getPage();
		int pageSize = queryCondition.getPer_page();
		return findForPager("dyFindForPager", parameter, curPage, pageSize);
	}
	@SuppressWarnings("unchecked")
	@Override
	public Pager<T> findForPager(QueryCondition queryCondition,String sqlId ){
		if(sqlId == null || "".equals(sqlId)) {
			throw new ApiException(1,"传入findForPager的查询SQLID为空");
		}else{
			//in case sqlId looks like ".dyFindForPager"
			sqlId.replaceAll(".", "");
		}
		Map<String,Object> parameter = (Map<String,Object>) JSON.toJSON(queryCondition);
		int curPage = queryCondition.getPage();
		int pageSize = queryCondition.getPer_page();
		return findForPager(sqlId, parameter, curPage, pageSize);
	
	}
	protected Pager<T> findForPager(String sqlId, Map<String,Object> parameter,Integer curPage, Integer pageSize)
			throws ApiException {
		
		Integer pageCount = null;
		if (curPage == 0){
			curPage = 1;
		}
			
		if (pageSize == 0){
			pageSize = 20;
		}
		
		int rowsCount = getSqlSession().selectOne(packageName+ "."+sqlId+"Count", parameter);
		try {
			pageCount = (( rowsCount /  pageSize) > (rowsCount / pageSize) ? ( rowsCount/ pageSize) + 1:rowsCount / pageSize);
			if(rowsCount%pageSize>0){
				pageCount =  pageCount+1;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		//变相解决查询所有
		if(pageSize !=null && pageSize.intValue() == -1){
			pageCount = 0;
			curPage = 1;
			pageSize = rowsCount;
		}
		List<T> list = null;
		if(((curPage - 1) * pageSize) < rowsCount){
			parameter.put("startNum", (curPage - 1) * pageSize);
			parameter.put("pageSize", pageSize); 
			list = getSqlSession().selectList(packageName + "."+sqlId,parameter);
		}
		//构建一个空list
		if(list == null){
			list = new ArrayList<T>();
		}
		Pager<T> pager = new Pager<T>();
		
		pager.setPageItems(list);
		pager.setRowsCount(rowsCount);
		pager.setCurrPage(curPage);
		pager.setPageCount(pageCount.intValue());
		pager.setPageSize(pageSize);

		if (list == null || list.isEmpty()) {
			pager.setPageRowsCount(0);
		} else {
			pager.setPageRowsCount(list.size());
		}
		return pager;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<Map<String, Object>> countWithGroupBy(QueryCondition queryCondition) throws ApiException {
		if(queryCondition == null ){
			 return new ArrayList<Map<String, Object>>();
		} 
		 Map<String,Object> parameter = (Map<String,Object>) JSON.toJSON(queryCondition);
		 List<Map<String, Object>>  list = getSqlSession().selectList(packageName + ".countWithGroupBy",parameter);
		 if(list == null){
			 return new ArrayList<Map<String, Object>>();
		 }
		
		return list;
	}
	
}
