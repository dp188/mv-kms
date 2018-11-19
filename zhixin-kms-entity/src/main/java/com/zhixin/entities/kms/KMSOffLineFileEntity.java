
package com.zhixin.entities.kms;

import java.io.Serializable;
import java.sql.Date;

import com.alibaba.fastjson.annotation.JSONField;
import com.zhixin.core.entities.AbstractEntity;


//离线文件
public class KMSOffLineFileEntity extends AbstractEntity<Long> implements Serializable {
	private static final long serialVersionUID = 1L;
		 		//
		private Long id ;
				 		//
		private String key ;
				 		//
		private Date createdtime ;
				 		//
		private Date createdtimeshow ;
				 		//文件名称
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
					 		public void setCreatedtime(Date  createdtime){
			this.createdtime = createdtime;
		}
		public Date getCreatedtime(){
			return this.createdtime;
		}
					 		public void setCreatedtimeshow(Date  createdtimeshow){
			this.createdtimeshow = createdtimeshow;
		}
		public Date getCreatedtimeshow(){
			return this.createdtimeshow;
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