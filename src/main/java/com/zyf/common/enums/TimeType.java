package com.zyf.common.enums;

public enum TimeType {

	year(1, "年"), 
	month(2, "月"), 
	day(3, "天"), 
	hour(4, "小时"), 
	minute(5, "分钟"), 
	second(6, "秒"), 
	millis(7, "毫秒"), 
	week(8, "周"), 
	;

	private int index;

	private String description;

	TimeType(int index, String description) {
		this.index = index;
		this.description = description;
	}

	public String getDescription() {
		return description;
	}

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	public void setDescription(String description) {
		this.description = description;
	}
}
