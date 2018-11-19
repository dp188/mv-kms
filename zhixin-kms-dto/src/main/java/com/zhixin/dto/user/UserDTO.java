package com.zhixin.dto.user;

import com.zhixin.core.dto.AbstractDTO;

public class UserDTO extends AbstractDTO {
	private String userName;
	private String password;

	@Override
	public Object getPK() {

		return null;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

}
