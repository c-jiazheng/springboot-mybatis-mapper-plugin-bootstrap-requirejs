package com.zyf.common.entity.sys;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.Min;
import java.io.Serializable;
import java.util.Calendar;

@ApiModel(value = "用户登录实体", description = "该实体用于管理用户属性")
@Table(name = "t_user")
public class User implements Serializable {
	private static final long serialVersionUID = 730863452165463427L;
	@Id
	@Min(value = -1, message = "ID不能小于-1")
	@ApiModelProperty(value = "id", required = true, dataType = "long", example = "1")
	private long id;
	@NotEmpty(message="账号不能为空")
	@ApiModelProperty(value = "账号", required = true, dataType = "String", example = "admin", access = "123", name = "321")
	private String account;
	@NotEmpty(message="密码不能为空")
	@Length(min=6,message="密码长度不能小于6位")
	@ApiModelProperty(value = "密码", required = true, dataType = "String", example = "admin")
	private String password;
	@ApiModelProperty(value = "真实姓名", required = true, dataType = "String", example = "管理员")
	private String name;
	@ApiModelProperty(value = "用户角色", required = true, dataType = "Long", example = "管理员角色")
	private long roleId;
	@ApiModelProperty(value = "创建日期", required = false, dataType = "Date", example = "2016-08-08")
	private Calendar createTime;
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getAccount() {
		return account;
	}
	public void setAccount(String account) {
		this.account = account;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Calendar getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Calendar createTime) {
		this.createTime = createTime;
	}

	public long getRoleId() {
		return roleId;
	}

	public void setRoleId(long roleId) {
		this.roleId = roleId;
	}
}