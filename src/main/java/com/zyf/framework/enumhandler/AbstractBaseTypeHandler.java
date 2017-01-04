package com.zyf.framework.enumhandler;

import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedTypes;
import org.apache.ibatis.type.TypeHandler;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public abstract class AbstractBaseTypeHandler extends BaseTypeHandler<CustomTypeEnum> implements TypeHandler<CustomTypeEnum> {

	private Class<CustomTypeEnum> customTypeEnum;
	private CustomTypeEnum[] allEnum;

	@SuppressWarnings("unchecked")
	protected AbstractBaseTypeHandler() {
		this.customTypeEnum = (Class<CustomTypeEnum>) (((MappedTypes) (getClass().getAnnotations()[0])).value()[0]);
		this.allEnum = customTypeEnum.getEnumConstants();
	}

	@Override
	public void setNonNullParameter(PreparedStatement ps, int i, CustomTypeEnum parameter, JdbcType jdbcType) throws SQLException {
		ps.setInt(i, parameter.getKey());
	}

	@Override
	public CustomTypeEnum getNullableResult(ResultSet rs, String columnName) throws SQLException {
		Integer value = rs.getInt(columnName);
		if (rs.wasNull())
			return null;
		for (CustomTypeEnum customTypeEnum : allEnum)
			if (value == customTypeEnum.getKey())
				return customTypeEnum;
		throw new SQLException("can't get customTypeEnum for class[" + customTypeEnum.getName() + "] with value[" + value + "]");
	}

	@Override
	public CustomTypeEnum getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
		Integer value = cs.getInt(columnIndex);
		if (cs.wasNull())
			return null;
		for (CustomTypeEnum customTypeEnum : allEnum)
			if (value == customTypeEnum.getKey())
				return customTypeEnum;
		throw new SQLException("can't get customTypeEnum for class[" + customTypeEnum.getName() + "] with value[" + value + "]");
	}

	@Override
	public CustomTypeEnum getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
		Integer value = rs.getInt(columnIndex);
		if (rs.wasNull())
			return null;
		for (CustomTypeEnum instance : allEnum)
			if (value == instance.getKey())
				return instance;
		throw new SQLException("can't get instance for class[" + customTypeEnum.getName() + "] with value[" + value + "]");
	}
}
