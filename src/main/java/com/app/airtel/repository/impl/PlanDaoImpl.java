package com.app.airtel.repository.impl;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;

import com.app.airtel.entity.Plan;
import com.app.airtel.exceptions.AirTelDataAccessException;
import com.app.airtel.exceptions.PlanNotFoundException;
import com.app.airtel.repository.AirTelBeanPropertySqlParameterSource;
import com.app.airtel.repository.BaseSpringDAO;
import com.app.airtel.repository.PlanDao;

@Repository
public class PlanDaoImpl extends BaseSpringDAO implements PlanDao {

	private static final Logger LOGGER = LoggerFactory.getLogger(PlanDaoImpl.class);
	private static final String SEQUNCE_NAME = "PlAN_SEQUENCE";

	private static final String INSERT_PLAN = "INSERT INTO PLAN(PLAN_ID, PLAN_NAME,AMOUNT,BALANCE,DATA,VALIDITY) VALUES(:planId, :planName,:amount,:balance,:data,:validity)";
	private static final String BATCH_INSERT_PLANS = "INSERT INTO PLAN(PLAN_ID, PLAN_NAME,AMOUNT,BALANCE,DATA,VALIDITY) VALUES(?,?,?,?,?,?)";
	private static final String SEARCH_PLAN_BY_ID = "SELECT PLAN_ID, PLAN_NAME,AMOUNT,BALANCE,DATA,VALIDITY FROM PLAN WHERE PLAN_ID = :planId";
	private static final String SEARCH_PLAN_BY_NAME = "SELECT PLAN_ID, PLAN_NAME,AMOUNT,BALANCE,DATA,VALIDITY FROM PLAN WHERE PLAN_NAME = :planName";
	private static final String SELECT_ALL_PLANS = "SELECT PLAN_ID, PLAN_NAME,AMOUNT,BALANCE,DATA,VALIDITY FROM PLAN";
	private static final String UPDATE_PLAN = "UPDATE PLAN SET PLAN_NAME=:planName,AMOUNT=:amount,BALANCE=:balance,DATA=:data,VALIDITY=:validity WHERE PLAN_ID=:planId";
	private static final String DELETE_PLAN_BY_ID = "DELETE FROM PLAN WHERE PLAN_ID=:planId";
	private static final String DELETE_PLAN_BY_NAME = "DELETE FROM PLAN WHERE PLAN_NAME=:planName";
	private static final String CHECK_PLAN_EXIST_WITH_NAME = "SELECT COUNT(*) AS COUNT FROM PLAN WHERE PLAN_NAME=:planName";

	@Override
	public void addPlan(Plan plan) {
		try {
			LOGGER.info("Inside addPlan() method from PlanDaoImpl");
			KeyHolder keyHolder = new GeneratedKeyHolder();
			plan.setPlanId(getSequenceNextValue(SEQUNCE_NAME));
			AirTelBeanPropertySqlParameterSource params = new AirTelBeanPropertySqlParameterSource(plan);

			getNamedParameterJdbcTemplate().update(INSERT_PLAN, params, keyHolder, new String[] { "PLAN_ID" });
			Number generatedPlanId = keyHolder.getKey();
			LOGGER.info("Generated Plan Id: {}", generatedPlanId);
		} catch (Exception ex) {
			throw new AirTelDataAccessException("Error occurred while saving Plan details", ex);
		}

	}

	@Override
	public void addPlans(List<Plan> planList) {
		try {
			LOGGER.info("Inside addPlans() method from PlanDaoImpl");

			getJdbcTemplate().batchUpdate(BATCH_INSERT_PLANS, new BatchPreparedStatementSetter() {

				@Override
				public void setValues(PreparedStatement ps, int i) throws SQLException {
					Plan plan = planList.get(i);
					int parameterIndex = 1;
					// PLAN(PLAN_NAME,AMOUNT,BALANCE,DATA,VALIDITY)
					ps.setLong(parameterIndex++, getSequenceNextValue(SEQUNCE_NAME));
					ps.setString(parameterIndex++, plan.getPlanName());
					ps.setInt(parameterIndex++, plan.getAmount());
					ps.setInt(parameterIndex++, plan.getBalance());
					ps.setInt(parameterIndex++, plan.getData());
					ps.setInt(parameterIndex, plan.getValidity());
				}

				@Override
				public int getBatchSize() {
					return planList.size();
				}
			});
		} catch (Exception ex) {
			LOGGER.error("Error occurred while saving plans batch insert: {}", ex);
			throw new AirTelDataAccessException("Error occurred while saving plans batch insert", ex);
		}
	}

	// Anonymous class implementation
	private static final RowMapper<Plan> PLAN_ROW_MAPPER = new RowMapper<Plan>() {
		@Override
		public Plan mapRow(ResultSet rs, int rowNum) throws SQLException {
			Plan plan = new Plan();
			plan.setPlanId(rs.getLong("PLAN_ID"));
			plan.setPlanName(rs.getString("PLAN_NAME"));
			plan.setAmount(rs.getInt("AMOUNT"));
			plan.setBalance(rs.getInt("BALANCE"));
			plan.setData(rs.getInt("DATA"));
			plan.setValidity(rs.getInt("VALIDITY"));
			return plan;
		}
	};

	// Lambda expression implementation
	private static final RowMapper<Plan> PLAN_ROW_MAPPER1 = (rs, rownum) -> {
		Plan plan = new Plan();
		plan.setPlanId(rs.getLong("PLAN_ID"));
		plan.setPlanName(rs.getString("PLAN_NAME"));
		plan.setAmount(rs.getInt("AMOUNT"));
		plan.setBalance(rs.getInt("BALANCE"));
		plan.setData(rs.getInt("DATA"));
		plan.setValidity(rs.getInt("VALIDITY"));
		return plan;
	};

	@Override
	public Optional<Plan> searchPlan(Long planId) {
		Assert.notNull(planId, "Plan Id should not be null");

		try {
			Optional<Plan> optional = null;

			Map<String, Object> params = new HashMap<>();
			params.put("planId", planId);

			Plan plan = getNamedParameterJdbcTemplate().queryForObject(SEARCH_PLAN_BY_ID, params, PLAN_ROW_MAPPER);
			if (plan.getPlanName() == null || plan.getPlanName().equals("")) {
				optional = Optional.empty();
			} else {
				optional = Optional.of(plan);
			}
			return optional;
		} catch (EmptyResultDataAccessException ex) {
			if (LOGGER.isDebugEnabled()) {
				LOGGER.error("No Plan found with given id: {}, exception: {}", planId, ex);
			}
			throw new PlanNotFoundException("No Plan found with given id: " + planId);
		} catch (Exception ex) {
			if (LOGGER.isDebugEnabled()) {
				LOGGER.error("Error occurred while searching the plan {}", ex);
			}
			throw new AirTelDataAccessException("Error occurred while searching the plan", ex);
		}

	}

	@Override
	public Optional<Plan> searchPlan(String planName) {
		Assert.hasText(planName, "Plan Name should not be null or empty");
		try {
			Optional<Plan> optional = null;
			MapSqlParameterSource params = new MapSqlParameterSource();
			params.addValue("planName", planName);

			Plan plan = getNamedParameterJdbcTemplate().queryForObject(SEARCH_PLAN_BY_NAME, params, PLAN_ROW_MAPPER);
			if (plan.getPlanName() == null || plan.getPlanName().equals("")) {
				optional = Optional.empty();
			} else {
				optional = Optional.of(plan);
			}
			return optional;
		} catch (EmptyResultDataAccessException ex) {
			if (LOGGER.isDebugEnabled()) {
				LOGGER.error("No Plan found with given plan name: {}, exception: {}", planName, ex);
			}
			throw new PlanNotFoundException("No Plan found with given plan name: " + planName);
		} catch (Exception ex) {
			if (LOGGER.isDebugEnabled()) {
				LOGGER.error("Error occurred while searching the plan {}", ex);
			}
			throw new AirTelDataAccessException("Error occurred while searching the plan", ex);
		}
	}

	@Override
	public List<Plan> getAllPlans() {
		try {
			return getJdbcTemplate().query(SELECT_ALL_PLANS, PLAN_ROW_MAPPER);
		} catch (Exception ex) {
			LOGGER.error("Error occurred while getting all plans {}", ex);
			throw new AirTelDataAccessException("Error occurred while getting all plans", ex);
		}
	}

	@Override
	public void updatePlan(Long planId, Plan plan) {
		try {
			LOGGER.info("Inside updatePlan() method from PlanDaoImpl");
			plan.setPlanId(planId);
			AirTelBeanPropertySqlParameterSource params = new AirTelBeanPropertySqlParameterSource(plan);

			getNamedParameterJdbcTemplate().update(UPDATE_PLAN, params);

		} catch (Exception ex) {
			LOGGER.error("Error occurred while updating Plan details: {}", ex);
			throw new AirTelDataAccessException("Error occurred while updating Plan details", ex);
		}
	}

	@Override
	public void deletePlan(Long planId) {
		try {
			MapSqlParameterSource params = new MapSqlParameterSource();
			params.addValue("planId", planId);

			getNamedParameterJdbcTemplate().update(DELETE_PLAN_BY_ID, params);

//		int i = getJdbcTemplate().update(DELETE_PLAN_BY_ID, params);
//		if (i >= 1) {
//			return true;
//		} else {
//			return false;
//		}
		} catch (Exception ex) {
			LOGGER.error("Error occurred while deleting the plan by id: {}", ex);
			throw new AirTelDataAccessException("Error occurred while deleting the plan by id", ex);
		}
	}

	@Override
	public void deletePlan(String planName) {
		try {
			// Instead of MapSqlParameterSource, we can also use HashMap
			Map<String, String> params = new HashMap<>();
			params.put("planName", planName);

			getNamedParameterJdbcTemplate().update(DELETE_PLAN_BY_NAME, params);
		} catch (Exception ex) {
			LOGGER.error("Error occurred while deleting the plan by name: {}", ex);
			throw new AirTelDataAccessException("Error occurred while deleting the plan by name", ex);
		}
	}

	@Override
	public boolean checkPlanNameExist(String planName) {
		try {
			MapSqlParameterSource params = new MapSqlParameterSource();
			params.addValue("planName", planName);

			Integer count = getNamedParameterJdbcTemplate().queryForObject(CHECK_PLAN_EXIST_WITH_NAME, params,
					Integer.class);
			return count == 0;
		} catch (Exception ex) {
			throw new AirTelDataAccessException(
					"Exception occurred in checkPlanNameExist() method in PlanDaoImpl class");
		}
	}

}
