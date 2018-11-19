package com.zhixin.dao.kms.impl;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import com.zhixin.dao.base.impl.AbstractBaseDao;
import com.zhixin.dao.kms.IKMSMany2ManyRoomDao;
import com.zhixin.entities.kms.KMSMany2ManyEntity;
@Repository
public class KMSMany2ManyRoomDaoIml extends AbstractBaseDao<KMSMany2ManyEntity, Long> implements IKMSMany2ManyRoomDao {

	@Qualifier("sqlSession")
	@Autowired
	private SqlSessionTemplate sqlSession = null;
	
	public KMSMany2ManyRoomDaoIml() {
		super(KMSMany2ManyRoomDaoIml.class.getName());
		// TODO Auto-generated constructor stub
	}

	public SqlSessionTemplate getSqlSession() {
		return sqlSession;
	}
	
	
}
