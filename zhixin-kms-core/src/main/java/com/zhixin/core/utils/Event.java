package com.zhixin.core.utils;

import java.util.List;


public class Event {

	private int action;
    private int dictStatus;
    private String actionDesc;
    private List<String> Operator;
    
	public int getAction() {
		return action;
	}
	public void setAction(int action) {
		this.action = action;
	}
	public int getDictStatus() {
		return dictStatus;
	}
	public void setDictStatus(int dictStatus) {
		this.dictStatus = dictStatus;
	}
	public String getActionDesc() {
		return actionDesc;
	}
	public void setActionDesc(String actionDesc) {
		this.actionDesc = actionDesc;
	}
	public List<String> getOperator() {
		return Operator;
	}
	public void setOperator(List<String> operator) {
		Operator = operator;
	}

   }
