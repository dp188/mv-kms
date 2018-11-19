package com.zhixin.service.kms.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zhixin.dao.kms.IKMSOffLineFileDao;
import com.zhixin.dto.kms.KMSOffLineFileDTO;
import com.zhixin.entities.kms.KMSOffLineFileEntity; 
import com.zhixin.service.kms.IKMSOffLineFileService; 
import com.zhixin.service.base.impl.AbstractBaseService;
 
@Service("KMSOffLineFileServiceImpl")
public class KMSOffLineFileServiceImpl extends 	AbstractBaseService<KMSOffLineFileDTO,KMSOffLineFileEntity, Long> implements IKMSOffLineFileService {

	@ Autowired
	public KMSOffLineFileServiceImpl(IKMSOffLineFileDao kMSOffLineFileDao ) {
		super(kMSOffLineFileDao);
	}
}
