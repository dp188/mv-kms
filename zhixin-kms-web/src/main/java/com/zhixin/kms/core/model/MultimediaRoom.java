package com.zhixin.kms.core.model;

import java.util.List;


/***
 * 多媒体房间
 * @author Coollf
 *
 */
public interface MultimediaRoom {
	
	/***
	 * 加入房间
	 * @param session
	 */
	public void join(MultimediaSession session);
	
	
	/***
	 * 离开房间
	 * @param session
	 */
	public void leave(MultimediaSession session);
	
	
	/***
	 * 关闭房间
	 * @param session
	 */
	public void close(MultimediaSession session);
	
	
	/***
	 * 获取 所有在线用户
	 * @return
	 */
	public List<MultimediaSession> getLiveUsers();
	
	
	
	/********
	 * 发送广播给房间里面的所有人
	 * @param msg
	 * @param userKey 发件人
	 * @param sendToSelf 是否抄送发件人一份
	 * @return
	 */
	public boolean sendBroadcast(Object msg,String userKey,boolean sendToSelf);
	
	
	/*****
	 * 发送点对点消息
	 * @param msg 消息
	 * @param senderUserKey 发件人
	 * @param receiverUserKey 收件人
	 * @return
	 */
	public boolean sendLetter(Object msg,String senderUserKey,String receiverUserKey);
	
	
	/***
	 * 获取当前主播
	 * @return
	 */
	public MultimediaSession getCurrentAnchor();
	
	
	
	/***
	 * 设置主播
	 * @param anchor
	 * @return
	 */
	public boolean setAnchor(MultimediaSession anchor);
	
	
	
	
	/*****
	 *  设置发言人
	 * @param anchor
	 * @return
	 */
	public boolean setSpeaker(MultimediaSession anchor);
	
	
	
	/****
	 * 获取当前发言人
	 * @return
	 */
	public MultimediaSession getCurrentSpeaker();

}
