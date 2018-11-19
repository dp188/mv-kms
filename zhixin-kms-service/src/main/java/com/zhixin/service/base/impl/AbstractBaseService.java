package com.zhixin.service.base.impl;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.BeanUtils;

import com.zhixin.core.annotation.TransformClassId;
import com.zhixin.core.annotation.TransformId;
import com.zhixin.core.common.exceptions.ApiException;
import com.zhixin.core.common.pager.Pager;
import com.zhixin.core.dto.AbstractDTO;
import com.zhixin.core.entities.AbstractEntity;
import com.zhixin.core.entities.query.QueryCondition;
import com.zhixin.core.enums.DataOperationEnum;
import com.zhixin.core.enums.ErrorCodeEnum;
import com.zhixin.core.enums.StatusEnum;
import com.zhixin.core.utils.DateUtil;
import com.zhixin.core.utils.PrimaryKeyGenerator;
import com.zhixin.core.utils.SpringContextUtil;
import com.zhixin.dao.base.IBaseDao;
import com.zhixin.service.base.IBaseService;

/**
 * 
 * @ClassName: AbstractBaseService
 * @Description: 基础服务类的抽象实现
 * @author zhangtiebin@bwcmall.com
 * @date 2015年6月23日 下午4:50:58
 * 
 */
public abstract class AbstractBaseService<Domain extends AbstractDTO, Entity extends AbstractEntity<PK>, PK extends Serializable> implements IBaseService<Domain, Entity, PK> {
	private IBaseDao<Entity, PK> businessDao = null;
	// cache class
	@SuppressWarnings("rawtypes")
	private Class domainClass = null;
	@SuppressWarnings("rawtypes")
	private Class entityClass = null;

	public AbstractBaseService(IBaseDao<Entity, PK> businessDao) {
		this.businessDao = businessDao;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public Entity copyFromDomain(Domain domain) {
		// 如果domain,直接返回null
		if (domain == null) {
			return null;
		}
		// 获取所有的泛型类型
		Class targetClass = getGenericType(1);
		Object target = null;
		try {
			target = targetClass.newInstance();
			BeanUtils.copyProperties(domain, target);
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}

		return (Entity) target;
	};

	// 查询domain中的transformID列表，更新成name
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void transformIdName(Object domain) {
		Class targetClass = domain.getClass();
		Object value = null;
		try {

			Field[] fields = targetClass.getDeclaredFields();

			for (Field field : fields) {
				if (!field.isAnnotationPresent(TransformId.class)) {
					continue;
				}

				TransformId transform = (TransformId) field
						.getAnnotation(TransformId.class);

				// 取到field值
				field.setAccessible(true);// 私有的设置访问权限
				value = field.get(domain);// 获取属性的value值

				if (value == null) {
					continue;
				}

				// 根据value值查询设置的service
				IBaseService service = (IBaseService) SpringContextUtil
						.getBean(transform.transformByService());

				// 通过该service查询对应到value值的domain数据
				Object obj = service.find((Serializable) value);
				if (obj == null) {
					continue;
				}

				Field objFeild = obj.getClass().getDeclaredField(
						transform.transformByfield());
				objFeild.setAccessible(true);
				value = objFeild.get(obj);

				// 将查询到得值赋给目标属性
				Field targetField = targetClass.getDeclaredField(transform
						.target());
				targetField.setAccessible(true);
				targetField.set(domain, value);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public Domain copyFromEntity(Entity entity) {
		// 如果entity,直接返回null
		if (entity == null) {
			return null;
		}
		// 获取所有的泛型类型
		Class targetClass = getGenericType(0);
		Object target = null;

		try {
			target = targetClass.newInstance();
			BeanUtils.copyProperties(entity, target);

			if (target instanceof AbstractDTO) {
				AbstractDTO domain = (AbstractDTO) target;
				// 转换时间
				domain.setCreatedTime(DateUtil.formatTimestamp(((AbstractEntity) entity).getCreatedTime()));
				domain.setUpdatedTime(DateUtil.formatTimestamp(((AbstractEntity) entity).getUpdatedTime())); 
				// 取到相应Domain中所有TransformId的注解
				if (targetClass.isAnnotationPresent(TransformClassId.class)) {
					transformIdName(target);
				}
			}

		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return (Domain) target;
	}

	@SuppressWarnings("rawtypes")
	public Class getGenericType(int index) {
		if (index == 0) {
			if (entityClass == null) {
				synchronized (this) {
					if (entityClass == null) {
						entityClass = getClass(index);
					}
				}
			}
			return entityClass;
		} else if (index == 1) {
			if (domainClass == null) {
				synchronized (this) {
					if (domainClass == null) {
						domainClass = getClass(index);
					}
				}
			}
			return domainClass;
		}
		return Object.class;

	}

	@SuppressWarnings("rawtypes")
	private Class getClass(int index) {
		Type genType = getClass().getGenericSuperclass();
		if (!(genType instanceof ParameterizedType)) {
			return Object.class;
		}
		Type[] params = ((ParameterizedType) genType).getActualTypeArguments();
		if (index >= params.length || index < 0) {
			throw new RuntimeException("Index outof bounds");
		}
		if (!(params[index] instanceof Class)) {
			return Object.class;
		}
		return (Class) params[index];
	}

	public List<Entity> copyFromDomainList(List<Domain> list) {
		List<Entity> resultList = new ArrayList<Entity>();
		if (list != null && list.size() > 0) {
			for (Domain domain : list) {
				resultList.add(copyFromDomain(domain));
			}
		}
		return resultList;
	}

	public List<Domain> copyFromEntityList(List<Entity> list) {
		List<Domain> resultList = new ArrayList<Domain>();
		if (list != null && list.size() > 0) {
			for (Entity entity : list) {
				resultList.add(copyFromEntity(entity));
			}
		}
		return resultList;
	}

	@SuppressWarnings("unchecked")
	@Override
	public PK create(Domain domain) throws ApiException {

		Entity entity = (Entity) copyFromDomain(domain);

		AbstractEntity<PK> abstractEntity = ((AbstractEntity<PK>) entity);
		
		if(abstractEntity.getPK() == null){
			//设置主键
			abstractEntity.setPK((PK) PrimaryKeyGenerator.SEQUENCE.next());
		}
		
		
		// 设置创建人和修改人
//		abstractEntity.setCreatedBy(SecurityContextHelper.getCurrentUserId());
//		abstractEntity.setUpdatedBy(SecurityContextHelper.getCurrentUserId());

		// 设置创建时间与修改时间
//		abstractEntity.setCreatedTime(S);
//		abstractEntity.setUpdatedTime(DateUtil.getCurrentSecond());

		// 判断是否需要设置排序字段
		if (abstractEntity.getSn() == null) {
			abstractEntity.setSn(1);
		}
		// 设置status状态
		abstractEntity.setStatus(StatusEnum.created.getValue());

		businessDao.create(entity);

		return abstractEntity.getPK();
	}

	@SuppressWarnings("unchecked")
	@Override
	public int update(Domain domain) throws ApiException {
		// 根据传入的dataOperation决定具体操作
		if (((AbstractDTO) domain).getDataOperation() == null
				|| ((AbstractDTO) domain).getDataOperation().equalsIgnoreCase(DataOperationEnum.OperationPatch.getAction())) {
			// 更新记录
			return _doUpdate(domain);
		} else if (((AbstractDTO) domain).getDataOperation().equalsIgnoreCase(DataOperationEnum.OperationDelete.getAction())) {
			// 删除该条数据
			remove((PK) domain.getPK()); 
		} else if (((AbstractDTO) domain).getDataOperation().equalsIgnoreCase(DataOperationEnum.OperationPost.getAction())) {
			// 新增，如果domain有特殊需要设置的字段，在上层需要设置好
			create(domain);
			return 1;
		}
		return -1;
	}

	protected int _doUpdate(Domain domain) throws ApiException {

		// 拷贝树形
		Entity entity = (Entity) copyFromDomain(domain);

		// 设置修改时间和修改人
//		abstractEntity.setUpdatedBy(SecurityContextHelper.getCurrentUserId());
//		abstractEntity.setUpdatedTime(DateUtil.getCurrentSecond());

		// 执行更新
		return businessDao.update(entity);
	}

	@Override
	public int remove(PK pk) throws ApiException {
		return businessDao.remove(pk);
	}

	@Override
	public Domain find(PK pk) throws ApiException {
		Entity entity = businessDao.find(pk);
		if (entity != null) {
			return copyFromEntity(entity);
		}
		return null;
	}
	@Override
	public List<Domain> find(List<PK> pkList) throws ApiException {
		List<Entity> entityList = businessDao.find(pkList);
		if (entityList != null && entityList.size()>0) {
			return copyFromEntityList(entityList);
		}
		return null;
	}


	@Override
	public List<Domain> findAll() throws ApiException {
		List<Entity> list = businessDao.findAll();
		return copyFromEntityList(list);
	}

	@Override
	public List<Domain> findByBeanProp(Domain domain) throws ApiException {
		List<Entity> list = businessDao.findByBeanProp(copyFromDomain(domain));
		return copyFromEntityList(list);
	}

	@Override
	public Pager<Domain> findForPager(Domain domain, Integer curPage,
			Integer pageSize) throws ApiException {
		Pager<Entity> pager = businessDao.findForPager(copyFromDomain(domain),
				curPage, pageSize);
		if (pager != null && pager.getPageItems() != null
				&& pager.getPageItems().size() > 0) {
			Pager<Domain> newpager = new Pager<Domain>();
			BeanUtils.copyProperties(pager, newpager);
			newpager.setPageItems(copyFromEntityList(pager.getPageItems()));
			return newpager;
		} else {
			Pager<Domain> emptyPager = new Pager<Domain>();
			List<Domain> emptyPageItems = new ArrayList<Domain>();
			if (pager != null) {
				BeanUtils.copyProperties(pager, emptyPager);
			}
			emptyPager.setPageItems(emptyPageItems);
			return emptyPager;
		}
	}

	@Override
	public List<Domain> create(List<Domain> domainList) throws ApiException {
		// 循环新增
		if (domainList != null && domainList.size() > 0) {
			for (Domain domain : domainList) {
				this.create(domain);
			}
		} else {
			throw new ApiException(ErrorCodeEnum.NOTNULL);
		}
		return domainList;
	}

	@Override
	public int update(List<Domain> domainList) throws ApiException {
		int rows = 0;
		// 循环修改
		if (domainList != null && domainList.size() > 0) {
			for (Domain domain : domainList) {
				rows =rows + this.update(domain);
			}
		} else {
			throw new ApiException(ErrorCodeEnum.NOTNULL);
		}
		return rows;
	}

	@Override
	public Pager<Domain> findForPager(QueryCondition queryCondition)
			throws ApiException {
		Pager<Entity> pager = businessDao.findForPager(queryCondition);
		if (pager != null && pager.getPageItems() != null
				&& pager.getPageItems().size() > 0) {
			Pager<Domain> newpager = new Pager<Domain>();
			BeanUtils.copyProperties(pager, newpager);
			newpager.setPageItems(copyFromEntityList(pager.getPageItems()));
			return newpager;
		} else {
			Pager<Domain> emptyPager = new Pager<Domain>();
			List<Domain> emptyPageItems = new ArrayList<Domain>();
			if (pager != null) {
				BeanUtils.copyProperties(pager, emptyPager);
			}
			emptyPager.setPageItems(emptyPageItems);
			return emptyPager;
		}
	}

	@Override
	public int remove(List<PK> pkList) throws ApiException {
		int rows = 0;
		// 循环遍历主键数组
		if (pkList != null && pkList.size() > 0) {
			for (PK pk : pkList) {
				rows = rows + this.remove(pk);
			}
		}
		return rows;
	}

	@Override
	public List<Map<String, Object>> countWithGroupBy(
			QueryCondition queryCondition) throws ApiException {
		if (queryCondition == null) {
			return new ArrayList<Map<String, Object>>();
		}
		// if (queryCondition.getGroup_by() == null
		// || queryCondition.getGroup_by().length == 0) {
		// throw new ApiException(ErrorCodeEnum.ParamsError.getCode(),
		// "必须指定groupby的字段");
		// }
		return businessDao.countWithGroupBy(queryCondition);
	}
	/**
	 * 根据主键更新状态
	 * @param id
	 * @param status
	 * @throws ApiException
	 */
	public void updateStatus(Long id,StatusEnum status)throws ApiException{
		 businessDao.updateStatus(id,status);
	}
}
