
package com.zhixin.entities.kms;

import java.io.Serializable;
import java.sql.Date;

import com.alibaba.fastjson.annotation.JSONField;
import com.zhixin.core.entities.AbstractEntity;


//字幕
public class KMSCaptionEntity extends AbstractEntity<Long> implements Serializable {
	private static final long serialVersionUID = 1L;
		 		//
		private Long id ;
				 		//
		private String key ;
				 		//
		private Date capTime ;
				 		//
		private Date capTimeShow ;
				 		//字幕
		private String caption ;
				 		//字幕所属文件
		private String filename ;
							 		public void setId(Long  id){
			this.id = id;
		}
		public Long getId(){
			return this.id;
		}
					 		public void setKey(String  key){
			this.key = key;
		}
		public String getKey(){
			return this.key;
		}
					 		public void setCapTime(Date  capTime){
			this.capTime = capTime;
		}
		public Date getCapTime(){
			return this.capTime;
		}
					 		public void setCapTimeShow(Date  capTimeShow){
			this.capTimeShow = capTimeShow;
		}
		public Date getCapTimeShow(){
			return this.capTimeShow;
		}
					 		public void setCaption(String  caption){
			this.caption = caption;
		}
		public String getCaption(){
			return this.caption;
		}
					 		public void setFilename(String  filename){
			this.filename = filename;
		}
		public String getFilename(){
			return this.filename;
		}
																									@JSONField(serialize = false)
	@Override
	public void setPK(Long pk) {
	 	this.id = pk;

	}

	@Override
	public Long getPK() {
	 
		return id;
	}
}