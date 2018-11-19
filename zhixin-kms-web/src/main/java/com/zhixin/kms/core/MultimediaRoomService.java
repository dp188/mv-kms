package com.zhixin.kms.core;

import com.zhixin.kms.core.model.MultimediaRoom;

/****
 * 多媒体房间服务
 * @author Coollf
 *
 */
public interface MultimediaRoomService {
	
	public MultimediaRoom getRoom(Long roomId);
	
	public MultimediaRoom createRoom(Long roomId);
}
