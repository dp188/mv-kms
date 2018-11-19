package com.zhixin.dao.kms.impl;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.stereotype.Repository;

import com.zhixin.dao.base.impl.AbstractBaseDao;
import com.zhixin.dao.kms.IKMSRoomDao;
import com.zhixin.entities.kms.KMSRoomEntity;

@CacheConfig(cacheNames = "KMSRoomDaoImpl", cacheResolver="PlatformCacheResolver", cacheManager="cacheManager")
@Repository
public class KMSRoomDaoImpl extends AbstractBaseDao<KMSRoomEntity, Long> implements IKMSRoomDao {
     
	@Qualifier("sqlSession")
	@Autowired
	private SqlSessionTemplate sqlSession = null;
	
    public KMSRoomDaoImpl() {
		super(KMSRoomDaoImpl.class.getName());
    }
	public  SqlSessionTemplate getSqlSession(){
		return sqlSession;
	}
 
}