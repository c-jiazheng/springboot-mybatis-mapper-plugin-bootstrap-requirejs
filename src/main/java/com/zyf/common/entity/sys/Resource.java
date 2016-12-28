package com.zyf.common.entity.sys;


import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Calendar;

@Table(name = "t_resource")
public class Resource implements Serializable{

	private static final long serialVersionUID = 3761143844780087351L;
	@Id
	private long id;
	private String resName;
	private String resUrl;
	private int resLevel;
	private String resPermission;
	private String resNo;
	private String parentResNo;
	private Calendar createTime;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getResName() {
		return resName;
	}

	public void setResName(String resName) {
		this.resName = resName;
	}

	public String getResUrl() {
		return resUrl;
	}

	public void setResUrl(String resUrl) {
		this.resUrl = resUrl;
	}

	public int getResLevel() {
		return resLevel;
	}

	public void setResLevel(int resLevel) {
		this.resLevel = resLevel;
	}

	public String getResNo() {
		return resNo;
	}

	public void setResNo(String resNo) {
		this.resNo = resNo;
	}

	public String getParentResNo() {
		return parentResNo;
	}

	public void setParentResNo(String parentResNo) {
		this.parentResNo = parentResNo;
	}

	public Calendar getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Calendar createTime) {
		this.createTime = createTime;
	}

	public String getResPermission() {
		return resPermission;
	}

	public void setResPermission(String resPermission) {
		this.resPermission = resPermission;
	}
}