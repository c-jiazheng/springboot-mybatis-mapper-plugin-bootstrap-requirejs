package com.zyf.common.entity.sys;

import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * Created by zengyufei on 2016/11/27/027.
 * QQ: 312636208
 */
@Table(name = "t_role")
public class Role implements Serializable{

	private static final long serialVersionUID = -4015226349078291825L;

	@Id
	private long id;
	private String roleName;
	private String description;
	private String resourceId;
	private String createTime;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getResourceId() {
		return resourceId;
	}

	public void setResourceId(String resourceId) {
		this.resourceId = resourceId;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
}
