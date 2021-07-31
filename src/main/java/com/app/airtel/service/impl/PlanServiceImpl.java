package com.app.airtel.service.impl;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.app.airtel.dto.PlanDTO;
import com.app.airtel.entity.Plan;
import com.app.airtel.exceptions.AirTelDataAccessException;
import com.app.airtel.exceptions.AirTelException;
import com.app.airtel.exceptions.DuplicatePlanException;
import com.app.airtel.exceptions.PlanNotFoundException;
import com.app.airtel.repository.PlanDao;
import com.app.airtel.service.PlanService;
import com.app.airtel.util.ResponseStatus;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class PlanServiceImpl implements PlanService {

	private static final Logger LOGGER = LoggerFactory.getLogger(PlanServiceImpl.class);

	@Autowired
	PlanDao planDao;

	@Override
	public ResponseStatus addPlan(PlanDTO planDTO) {
		if (!isDuplicatePlan(true, planDTO)) {
			try {
				planDao.addPlan(preparePlanEntity(planDTO));
				return prepareResponseStatus(HttpStatus.CREATED, "Plan added successfully");
			} catch (Exception ex) {
				LOGGER.error("Exception occurred while adding the plan in PlanServiceImpl - addPlan(): {} ", ex);
				return prepareResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to add Plan");
			}
		} else {
			throw new DuplicatePlanException("Plan already exist with name: " + planDTO.getPlanName());
		}
	}

	@Override
	public ResponseStatus addPlans(List<PlanDTO> planDTOList) {
		Assert.notEmpty(planDTOList, "Plan list is empty");

		List<Plan> planList = new ArrayList<>();

		planDTOList.forEach(planDTO -> {
			if (!isDuplicatePlan(true, planDTO)) {
				planList.add(preparePlanEntity(planDTO));
			}
		});

		try {
			planDao.addPlans(planList);
			return prepareResponseStatus(HttpStatus.CREATED, "Plans added successfully");
		} catch (Exception ex) {
			LOGGER.error("Exception occurred while adding the plans in PlanServiceImpl - addPlans(): {} ", ex);
			return prepareResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to add Plans");
		}
	}

	@Override
	public PlanDTO searchPlan(Long planId) {
		Optional<Plan> optional = planDao.searchPlan(planId);
		if (!optional.isPresent()) {
			throw new PlanNotFoundException("No Plan found with id:" + planId);
		}
		Plan plan = optional.get();
		return preparePlanDTO(plan);
	}

	@Override
	public PlanDTO searchPlan(String planName) {
		Optional<Plan> optional = planDao.searchPlan(planName);
		if (!optional.isPresent()) {
			throw new PlanNotFoundException("No Plan found with name:" + planName);
		}
		Plan plan = optional.get();
		return preparePlanDTO(plan);
	}

	@Override
	public List<PlanDTO> getAllPlans() {
		try {
			List<Plan> plans = planDao.getAllPlans();
			List<PlanDTO> planDTOList = new ArrayList<>();
			plans.forEach(plan -> {
				planDTOList.add(preparePlanDTO(plan));
			});
			return planDTOList;
		} catch (Exception ex) {
			LOGGER.error("Exception occurred while fetching all plans in  PlanServiceImpl - getAllPlans(): {} ", ex);
			throw new AirTelException("Exception occurred while fetching the Plan");
		}
	}

	@Override
	public ResponseStatus updatePlan(PlanDTO planDTO) {
		if (!isDuplicatePlan(false, planDTO)) {
			try {
				planDao.updatePlan(planDTO.getPlanId(), preparePlanEntity(planDTO));
				return prepareResponseStatus(HttpStatus.OK, "Plan updated successfully");
			} catch (Exception ex) {
				LOGGER.error("Exception occurred while updating the plan in PlanServiceImpl - updatePlan(): {} ", ex);
				return prepareResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to update Plan");
			}
		} else {
			throw new DuplicatePlanException("Plan already exist with name: " + planDTO.getPlanName());
		}
	}

	@Override
	public ResponseStatus deletePlan(Long planId) {
		try {
			planDao.deletePlan(planId);
			return prepareResponseStatus(HttpStatus.OK, "Plan deleted successfully");
		} catch (Exception ex) {
			throw new AirTelDataAccessException("Error occurred while deleting the plan by id", ex);
		}
	}

	@Override
	public ResponseStatus deletePlan(String planName) {
		try {
			planDao.deletePlan(planName);
			return prepareResponseStatus(HttpStatus.OK, "Plan deleted successfully");
		} catch (Exception ex) {
			throw new AirTelDataAccessException("Error occurred while deleting the plan by name", ex);
		}
	}

	@Override
	public ResponseStatus addBasicPlans() {
		try {
			ObjectMapper mapper = new ObjectMapper();
			Resource resource = new ClassPathResource("sql/plans-data.json");
			InputStream inputStream = resource.getInputStream();
			TypeReference<List<PlanDTO>> typeReference = new TypeReference<List<PlanDTO>>() {
			};

			List<PlanDTO> planList = mapper.readValue(inputStream, typeReference);

			return addPlans(planList);

		} catch (Exception e) {
			LOGGER.error(e.getMessage());
			return prepareResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR,
					"Error occured while inserting the basic plans");
		}
	}

	private PlanDTO  preparePlanDTO(Plan plan) {
		PlanDTO planDTO = new PlanDTO();
		BeanUtils.copyProperties(plan, planDTO);
		return planDTO;
	}

	private Plan preparePlanEntity(PlanDTO planDTO) {
		Plan plan = new Plan();
		BeanUtils.copyProperties(planDTO, plan);
		return plan;
	}

	private ResponseStatus prepareResponseStatus(HttpStatus httpStatus, String message) {
		ResponseStatus responseStatus = new ResponseStatus();
		responseStatus.setStatusCode(String.valueOf(httpStatus.value()));
		responseStatus.setMessage(message);
		return responseStatus;
	}

	private boolean isDuplicatePlan(boolean newFlag, PlanDTO planDTO) {
		try {
			Optional<Plan> optional = planDao.searchPlan(planDTO.getPlanName());
			if (!optional.isPresent()) {
				return false;
			} else {
				Plan plan = optional.get();
				if (newFlag || plan.getPlanId() != planDTO.getPlanId()) {
					return true;
				}
			}
			return false;
		} catch (PlanNotFoundException ex) {
			return false;
		} catch (Exception ex) {
			throw new AirTelException("Exception occured while checking duplicates");
		}

	}

}
