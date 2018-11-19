package com.zhixin.dao.task.impl;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.stereotype.Repository;

import com.zhixin.dao.base.impl.AbstractBaseDao;
import com.zhixin.dao.task.IFMPTaskDao;
import com.zhixin.entities.task.FMPTaskEntity;

@CacheConfig(cacheNames = "FMPTaskDaoImpl", cacheResolver="PlatformCacheResolver", cacheManager="cacheManager")
@Repository
public class FMPTaskDaoImpl extends AbstractBaseDao<FMPTaskEntity, Long> implements IFMPTaskDao {
     
	@Qualifier("sqlSession")
	@Autowired
	private SqlSessionTemplate sqlSession = null;
	
    public FMPTaskDaoImpl() {
		super(FMPTaskDaoImpl.class.getName());
    }
	public  SqlSessionTemplate getSqlSession(){
		return sqlSession;
	}
 
}