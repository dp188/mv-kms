package com.zhixin.dao.kms.impl;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.stereotype.Repository;

import com.zhixin.dao.base.impl.AbstractBaseDao;
import com.zhixin.dao.kms.IKMSCaptionDao;
import com.zhixin.entities.kms.KMSCaptionEntity;

@CacheConfig(cacheNames = "KMSCaptionDaoImpl", cacheResolver="PlatformCacheResolver", cacheManager="cacheManager")
@Repository
public class KMSCaptionDaoImpl extends AbstractBaseDao<KMSCaptionEntity, Long> implements IKMSCaptionDao {
     
	@Qualifier("sqlSession")
	@Autowired
	private SqlSessionTemplate sqlSession = null;
	
    public KMSCaptionDaoImpl() {
		super(KMSCaptionDaoImpl.class.getName());
    }
	public  SqlSessionTemplate getSqlSession(){
		return sqlSession;
	}
 
}