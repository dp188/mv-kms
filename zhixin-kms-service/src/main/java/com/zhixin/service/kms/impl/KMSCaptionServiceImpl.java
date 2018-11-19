package com.zhixin.service.kms.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zhixin.dao.kms.IKMSCaptionDao;
import com.zhixin.dto.kms.KMSCaptionDTO;
import com.zhixin.entities.kms.KMSCaptionEntity; 
import com.zhixin.service.kms.IKMSCaptionService; 
import com.zhixin.service.base.impl.AbstractBaseService;
 
@Service("KMSCaptionServiceImpl")
public class KMSCaptionServiceImpl extends 	AbstractBaseService<KMSCaptionDTO,KMSCaptionEntity, Long> implements IKMSCaptionService {

	@ Autowired
	public KMSCaptionServiceImpl(IKMSCaptionDao kMSCaptionDao ) {
		super(kMSCaptionDao);
	}
}
