package com.app.airtel.repository;

import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import org.apache.logging.log4j.core.lookup.StrSubstitutor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcDaoSupport;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import com.app.airtel.exceptions.AirTelDataAccessException;

@Repository
public class BaseSpringDAO {

	private NamedParameterJdbcDaoSupport jdbcSupport = null;
	private int fetchSize = 500;
	private int queryTimeout = 300;

	@Autowired
	public void setDataSource(DataSource dataSource) {
		this.jdbcSupport = new NamedParameterJdbcDaoSupport();
		jdbcSupport.setDataSource(dataSource);
	}

	public int getFetchSize() {
		return fetchSize;
	}

	public void setFetchSize(int fetchSize) {
		this.fetchSize = fetchSize;
	}

	public void setProperties(JdbcTemplate jdbcTemplate) {
		jdbcTemplate.setFetchSize(fetchSize);
		jdbcTemplate.setQueryTimeout(queryTimeout);
	}

	protected JdbcTemplate getJdbcTemplate() {
		JdbcTemplate template = jdbcSupport.getJdbcTemplate();
		setProperties(template);
		return template;
	}

	protected NamedParameterJdbcTemplate getNamedParameterJdbcTemplate() {
		NamedParameterJdbcTemplate template = jdbcSupport.getNamedParameterJdbcTemplate();
		setProperties(template.getJdbcTemplate());
		return template;
	}

	private static final String SEQ_NEXT_VALUE_SQL = "SELECT ${seq_name}.NEXTVAL FROM DUAL";
	protected Long getSequenceNextValue(String sequenceName) {
		Map<String, String> params = new HashMap<>();
		params.put("seq_name", sequenceName);
		StrSubstitutor subs = new StrSubstitutor(params);
		try {
			return getNamedParameterJdbcTemplate()
					.queryForObject(subs.replace(SEQ_NEXT_VALUE_SQL), 
							new HashMap<>(),Long.class);
		} catch (Exception ex) {
			throw new AirTelDataAccessException(
					String.format("Exception while fetching the sequence next value for sequnce %s", sequenceName), ex);
		}

	}

}
