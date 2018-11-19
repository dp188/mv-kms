package com.zhixin.service.kms.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zhixin.dao.kms.IKMSRoomDao;
import com.zhixin.dto.kms.KMSRoomDTO;
import com.zhixin.entities.kms.KMSRoomEntity; 
import com.zhixin.service.kms.IKMSRoomService; 
import com.zhixin.service.base.impl.AbstractBaseService;
 
@Service("KMSRoomServiceImpl")
public class KMSRoomServiceImpl extends 	AbstractBaseService<KMSRoomDTO,KMSRoomEntity, Long> implements IKMSRoomService {

	@ Autowired
	public KMSRoomServiceImpl(IKMSRoomDao kMSRoomDao ) {
		super(kMSRoomDao);
	}
}
