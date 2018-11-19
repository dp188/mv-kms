package com.zhixin.kms.core.model;

import org.kurento.client.WebRtcEndpoint;


public interface MultimediaSession {
	
	/********
	 * 获取视频输入流,即流来源
	 * @return
	 */
	public WebRtcEndpoint getInputVideo();
	
	/****
	 * 获取视频输入流,即流输出()
	 * @return
	 */
	public WebRtcEndpoint getOutputVideo();
	
	
	/***
	 * 获取音频输入流(上一级别)
	 * @return
	 */
	public WebRtcEndpoint getInputVoice();
	
	
	/****
	 * 获取视频输出流(输出到前台或者,文件记录)
	 * @return
	 */
	public WebRtcEndpoint getOutputVoice();

}
