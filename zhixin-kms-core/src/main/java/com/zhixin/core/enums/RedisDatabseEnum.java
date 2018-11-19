package com.zhixin.core.enums;

/**
 * 
* @ClassName: RedisDatabseEnum 
* @Description: redis缓存数据库定义
* @author zhangtiebin@bwcmall.com
* @date 2015年7月6日 下午8:41:13 
*
 */
public enum RedisDatabseEnum {
	
	SYS(0,"系统缓存"),
	SESSION(1,"SESSION缓存"),
	DICT(2,"字典缓存"),
    META(3,"元数据缓存-YOP等的基础数据缓存"),
    Cust(4,"Cust缓存"),
    Project(5,"Project缓存");
    private final int databaseId;
    private String name;

    RedisDatabseEnum(int databaseId, String name) {
        this.databaseId = databaseId;
        this.name=name;
    }
	public int getDatabaseId() {
		return databaseId;
	} 
	public String getName() {
		return name;
	}

}

