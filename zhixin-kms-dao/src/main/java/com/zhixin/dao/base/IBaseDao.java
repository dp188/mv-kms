package com.zhixin.dao.base;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import com.zhixin.core.common.exceptions.ApiException;
import com.zhixin.core.common.pager.Pager;
import com.zhixin.core.entities.query.QueryCondition;
import com.zhixin.core.enums.StatusEnum;

/**
 * Created by tonymac on 14/11/17.
 */
public interface IBaseDao<T, PK extends Serializable> {
	/**
	 *  新增
	 * @param entity
	 * @return
	 */
	public PK create(T entity);
	/**
	 * 查找所有的记录
	 * @param entity
	 * @return
	 */
	public List<T> findAll();

	/**
	 * 更新记录
	 * @param entity
	 */
	public int update(T entity);

	/**
	 * 根据主键删除记录
	 * @param pk
	 */
	public int remove(PK pk);

	/**
	 * 根据主键查找记录
	 * @param pk
	 * @return
	 */
	public T find(PK pk);
	
	/**
	 * 根据主键List查找记录
	 * @param pkList
	 * @return
	 */
	public List<T> find(List<PK> pkList);

	/**
	 * 根据实体成员属性查找记录
	 * @param entity
	 * @return
	 */
	public List<T> findByBeanProp(T entity);
	
	/**
	 *  根据实体成员属性做分页查询
	 * @param entity
	 * @param curPage
	 * @param pageSize
	 * @return
	 */
	public Pager<T> findForPager(T entity,Integer curPage,Integer pageSize);
	
	/**
	 * 动态查询
	 * @param queryCondition
	 * @return
	 * @throws ApiException
	 */
	public Pager<T> findForPager(QueryCondition queryCondition)
			throws ApiException;

	/**
	 * 动态查询
	 * @param queryCondition
	 * @param sqlId
	 * @return
	 * @throws ApiException
	 */
	public Pager<T> findForPager(QueryCondition queryCondition,String sqlId )
			throws ApiException;
	/**
	 * 计算被group的字段的count
	 * @param queryCondition
	 * @return
	 * @throws ApiException
	 */
	public List<Map<String,Object>> countWithGroupBy(QueryCondition queryCondition)throws ApiException;
	
	/**
	 * 根据主键更新状态
	 * @param id
	 * @param status
	 */
	public void updateStatus(Long id, StatusEnum status);
	
}
