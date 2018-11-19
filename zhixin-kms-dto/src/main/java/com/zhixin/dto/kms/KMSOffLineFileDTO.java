package com.zhixin.dto.kms;

import java.io.Serializable;
import java.sql.Date;

import com.zhixin.core.dto.AbstractDTO;


//离线文件
public class KMSOffLineFileDTO  extends AbstractDTO  implements Serializable {
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
				@Override
	public Long getPK() {
		return id;
	}
}

