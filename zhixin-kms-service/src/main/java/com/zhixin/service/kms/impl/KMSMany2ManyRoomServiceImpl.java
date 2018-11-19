package com.zhixin.service.kms.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zhixin.dao.kms.IKMSMany2ManyRoomDao;
import com.zhixin.dto.kms.KMSMany2ManyDTO;
import com.zhixin.entities.kms.KMSMany2ManyEntity;
import com.zhixin.service.base.impl.AbstractBaseService;
import com.zhixin.service.kms.IKMSMany2ManyRoomService;

@Service("IKMSMany2ManyRoomService")
public class KMSMany2ManyRoomServiceImpl extends AbstractBaseService<KMSMany2ManyDTO, KMSMany2ManyEntity, Long> implements IKMSMany2ManyRoomService {

	@ Autowired
	public KMSMany2ManyRoomServiceImpl(IKMSMany2ManyRoomDao businessDao) {
		super(businessDao);
	}
}
