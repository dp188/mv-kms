package com.zhixin.service.base;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import com.zhixin.core.common.exceptions.ApiException;
import com.zhixin.core.common.pager.Pager;
import com.zhixin.core.entities.query.QueryCondition;
import com.zhixin.core.enums.StatusEnum;

/**
 * 
* @ClassName: IBaseService 
* @Description: 基本服务接口
* @author zhangtiebin@bwcmall.com
* @date 2015年6月23日 下午4:40:50 
*
 */
public interface IBaseService<Domain, Entity,PK extends Serializable> {
	/**
	 * 创建一个对象
	 * @param domain
	 * @return
	 * @throws ApiException
	 */
	public PK create(Domain domain) throws ApiException;
	
	/**
	 * 创建一组对象
	 * @param domainList
	 * @return
	 * @throws ApiException
	 */
	public List<Domain> create(List<Domain> domainList) throws ApiException;
	
	/**
	 * 更新一个对象
	 * @param domain
	 * @throws ApiException
	 */
	public int update(Domain domain) throws ApiException;

	/**
	 * 更新多个对象
	 * @param domainlist
	 * @throws ApiException
	 */
	public int update(List<Domain> domainList) throws ApiException;
	
	/**
	 * 根据主键删除一个对象
	 * @param pk
	 * @throws ApiException
	 */
	public int remove(PK pk) throws ApiException;
	
	

	/**
	 * 根据主键删除一组对象
	 * @param pk
	 * @throws ApiException
	 */
	public int remove(List<PK> pkList) throws ApiException;
	
	

	/**
	 * 根据主键查询一个对象
	 * @param pk
	 * @return
	 * @throws ApiException
	 */
	public Domain find(PK pk) throws ApiException;
	
	/**
	 * 根据主键List查找记录
	 * @param pkList
	 * @return
	 */
	public List<Domain> find(List<PK> pkList) throws ApiException;
	
	/**
	 * 根据主键查询一个对象 
	 * @return
	 * @throws ApiException
	 */
	public List<Domain> findAll() throws ApiException;
	
	/**
	 * 根据数学查询数据
	 * @param domain
	 * @return
	 * @throws ApiException
	 */
	public List<Domain> findByBeanProp(Domain domain) throws ApiException;
	/**
	 * 分页查询
	 * @param domain
	 * @param curPage
	 * @param pageSize
	 * @return
	 * @throws ApiException
	 */
	public Pager<Domain> findForPager(Domain domain,Integer curPage,Integer pageSize)throws ApiException;
	
	/**
	 * 分页查询
	 * @param queryCondition 
	 * @return
	 * @throws ApiException
	 */
	public Pager<Domain> findForPager(QueryCondition queryCondition)throws ApiException;
	
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
	 * @throws ApiException
	 */
	public void updateStatus(Long id,StatusEnum status)throws ApiException;
	
}
