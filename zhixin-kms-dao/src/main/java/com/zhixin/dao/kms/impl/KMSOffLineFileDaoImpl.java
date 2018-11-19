package com.zhixin.dao.kms.impl;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.stereotype.Repository;

import com.zhixin.dao.base.impl.AbstractBaseDao;
import com.zhixin.dao.kms.IKMSOffLineFileDao;
import com.zhixin.entities.kms.KMSOffLineFileEntity;

@CacheConfig(cacheNames = "KMSOffLineFileDaoImpl", cacheResolver="PlatformCacheResolver", cacheManager="cacheManager")
@Repository
public class KMSOffLineFileDaoImpl extends AbstractBaseDao<KMSOffLineFileEntity, Long> implements IKMSOffLineFileDao {
     
	@Qualifier("sqlSession")
	@Autowired
	private SqlSessionTemplate sqlSession = null;
	
    public KMSOffLineFileDaoImpl() {
		super(KMSOffLineFileDaoImpl.class.getName());
    }
	public  SqlSessionTemplate getSqlSession(){
		return sqlSession;
	}
 
}