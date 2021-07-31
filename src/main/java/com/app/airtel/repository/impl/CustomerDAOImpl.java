package com.app.airtel.repository.impl;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.stereotype.Repository;

import com.app.airtel.entity.Customer;
import com.app.airtel.repository.AirTelBeanPropertySqlParameterSource;
import com.app.airtel.repository.BaseSpringDAO;
import com.app.airtel.repository.CustomerDAO;


@Repository
public class CustomerDAOImpl extends BaseSpringDAO implements CustomerDAO {

	private static final Logger LOGGER = LoggerFactory.getLogger(CustomerDAOImpl.class);

	private static final String INSERT_CUSTOMER = "INSERT INTO CUSTOMER(PHONE_NUMBER, CUSTOMER_NAME,AGE,GENDER,ADDRESS,PLAN_ID) VALUES(:phoneNumber, :name,:age,:gender,:address,:planId)";
	private static final String BATCH_INSERT_CUSTOMERS = "INSERT INTO CUSTOMER(PHONE_NUMBER, CUSTOMER_NAME,AGE,GENDER,ADDRESS,PLAN_ID) VALUES(?,?,?,?,?,?)";
	private static final String SEARCH_CUSTOMER_BY_ID = "SELECT PHONE_NUMBER, CUSTOMER_NAME,AGE,GENDER,ADDRESS,PLAN_ID FROM CUSTOMER WHERE PHONE_NUMBER = :phoneNumber";
	private static final String SEARCH_CUSTOMER_BY_NAME = "SELECT PHONE_NUMBER, CUSTOMER_NAME,AGE,GENDER,ADDRESS,PLAN_ID FROM CUSTOMER WHERE CUSTOMER_NAME = :name";

	private static final String SELECT_ALL_CUSTOMERS = "SELECT PHONE_NUMBER, CUSTOMER_NAME,AGE,GENDER,ADDRESS,PLAN_ID FROM CUSTOMER";

	private static final String UPDATE_CUSTOMER = "UPDATE CUSTOMER SET CUSTOMER_NAME=:name,AGE=:age,GENDER=:gender,ADDRESS=:address,PLAN_ID=:planId WHERE PHONE_NUMBER=:phoneNumber";
	private static final String DELETE_CUSTOMER_BY_ID = "DELETE FROM CUSTOMER WHERE PHONE_NUMBER=:phoneNumber";
	private static final String DELETE_CUSTOMER_BY_NAME = "DELETE FROM CUSTOMER WHERE CUSTOMER_NAME=:name";
	private static final String CHECK_CUSTOMER_EXIST_WITH_NAME = "SELECT COUNT(*) AS COUNT FROM CUSTOMER WHERE CUSTOMER_NAME=:name";

	@Override
	public void addCustomer(Customer customer) {
		LOGGER.info("Inside addCustomer() method from CustomerDAOImpl");
		BeanPropertySqlParameterSource parameterSource = new AirTelBeanPropertySqlParameterSource(customer);
		getNamedParameterJdbcTemplate().update(INSERT_CUSTOMER, parameterSource);
	}

	@Override
	public void addCustomers(List<Customer> customerList) {
		LOGGER.info("Inside addCustomers() method from CustomerDAOImpl");
		getJdbcTemplate().batchUpdate(BATCH_INSERT_CUSTOMERS, new BatchPreparedStatementSetter() {

			@Override
			public void setValues(PreparedStatement ps, int i) throws SQLException {
				Customer customer = customerList.get(i);
				int parameterIndex = 1;
				// PHONE_NUMBER, CUSTOMER_NAME,AGE,GENDER,ADDRESS,PLAN_ID
				ps.setLong(parameterIndex++, customer.getPhoneNumber());
				ps.setString(parameterIndex++, customer.getName());
				ps.setInt(parameterIndex++, customer.getAge());
				ps.setString(parameterIndex++, customer.getGender());
				ps.setString(parameterIndex++, customer.getAddress());
				ps.setInt(parameterIndex, customer.getPlanId());
			}

			@Override
			public int getBatchSize() {
				return customerList.size();
			}
		});
	}

	@Override
	public Customer searchCustomer(Long phoneNumber) {
		LOGGER.info("Inside searchCustomer(phoneNumber) method from CustomerDAOImpl");
		MapSqlParameterSource parameterSource = new MapSqlParameterSource();
		parameterSource.addValue("phoneNumber", phoneNumber);
		return getNamedParameterJdbcTemplate().queryForObject(SEARCH_CUSTOMER_BY_ID, parameterSource,
				new RowMapper<Customer>() {
					@Override
					public Customer mapRow(ResultSet rs, int rowNum) throws SQLException {
						Customer customer = new Customer();
						customer.setPhoneNumber(rs.getLong("PHONE_NUMBER"));
						customer.setName(rs.getString("CUSTOMER_NAME"));
						customer.setAddress(rs.getString("ADDRESS"));
						customer.setAge(rs.getInt("AGE"));
						customer.setGender(rs.getString("GENDER"));
						customer.setPlanId(rs.getInt("PLAN_ID"));

						return customer;
					}
				});
	}

	@Override
	public Customer searchCustomer(String name) {
		LOGGER.info("Inside searchCustomer(name) method from CustomerDAOImpl");
		Map<String, String> params = new HashMap<>();
		params.put("name", name);
		return getNamedParameterJdbcTemplate().queryForObject(SEARCH_CUSTOMER_BY_NAME, params, CUSTOMER_ROW_MAPPER);
	}

	// Without java 8
	private static final RowMapper<Customer> CUSTOMER_ROW_MAPPER=new RowMapper<Customer>(){@Override public Customer mapRow(ResultSet rs,int rowNum)throws SQLException{Customer customer=new Customer();customer.setPhoneNumber(rs.getLong("PHONE_NUMBER"));customer.setName(rs.getString("CUSTOMER_NAME"));customer.setAddress(rs.getString("ADDRESS"));customer.setAge(rs.getInt("AGE"));customer.setGender(rs.getString("GENDER"));customer.setPlanId(rs.getInt("PLAN_ID"));return customer;}};

	// With Java 8
	private static final RowMapper<Customer> CUSTOMER_ROW_MAPPER1 = (rs, rowNum) -> {
		Customer customer = new Customer();
		customer.setPhoneNumber(rs.getLong("PHONE_NUMBER"));
		customer.setName(rs.getString("CUSTOMER_NAME"));
		customer.setAddress(rs.getString("ADDRESS"));
		customer.setAge(rs.getInt("AGE"));
		customer.setGender(rs.getString("GENDER"));
		customer.setPlanId(rs.getInt("PLAN_ID"));
		return customer;
	};

	@Override
	public List<Customer> getAllCustomers() {
		LOGGER.info("Inside getAllCustomers() method from CustomerDAOImpl");
		return getNamedParameterJdbcTemplate().query(SELECT_ALL_CUSTOMERS, CUSTOMER_ROW_MAPPER1);
	}

	@Override
	public void updateCustomer(Long phoneNumber, Customer customer) {
		LOGGER.info("Inside updateCustomer() method from CustomerDAOImpl");
		BeanPropertySqlParameterSource parameterSource = new AirTelBeanPropertySqlParameterSource(customer);
		getNamedParameterJdbcTemplate().update(UPDATE_CUSTOMER, parameterSource);
	}

	@Override
	public void deleteCustomer(Long phoneNumber) {
		LOGGER.info("Inside deleteCustomer(phoneNumber) method from CustomerDAOImpl");
		MapSqlParameterSource parameterSource = new MapSqlParameterSource();
		parameterSource.addValue("phoneNumber", phoneNumber);
		getNamedParameterJdbcTemplate().update(DELETE_CUSTOMER_BY_ID, parameterSource);
	}

	@Override
	public void deleteCustomer(String name) {
		LOGGER.info("Inside deleteCustomer(name) method from CustomerDAOImpl");
		MapSqlParameterSource parameterSource = new MapSqlParameterSource();
		parameterSource.addValue("name", name);
		getNamedParameterJdbcTemplate().update(DELETE_CUSTOMER_BY_NAME, parameterSource);
	}
}


