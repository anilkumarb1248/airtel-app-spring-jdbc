package com.app.airtel.repository;

import java.sql.Types;
import java.time.LocalDate;

import org.springframework.beans.BeanWrapper;
import org.springframework.beans.PropertyAccessorFactory;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;

public class AirTelBeanPropertySqlParameterSource extends BeanPropertySqlParameterSource {

	private final BeanWrapper beanWrapper;

	public AirTelBeanPropertySqlParameterSource(Object object) {
		super(object);
		this.beanWrapper = PropertyAccessorFactory.forBeanPropertyAccess(object);
	}

	@Override
	public int getSqlType(String paramName) {
		int sqlType = super.getSqlType(paramName);
		if (sqlType != TYPE_UNKNOWN) {
			return sqlType;
		}

		Class<?> propType = this.beanWrapper.getPropertyType(paramName);
		if (Enum.class.isAssignableFrom(propType)) {
			return Types.VARCHAR;
		}
		return TYPE_UNKNOWN;
	}

	@Override
	public Object getValue(String paramName) throws IllegalArgumentException {
		Object result = super.getValue(paramName);
		if (result instanceof LocalDate) {
			return java.sql.Date.valueOf((LocalDate) result);
		} else {
			return result;
		}
	}

}
