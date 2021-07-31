package com.app.airtel.controller.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import com.app.airtel.controller.PlanController;
import com.app.airtel.dto.PlanDTO;
import com.app.airtel.service.PlanService;
import com.app.airtel.util.ResponseStatus;

@RestController
public class PlanControllerImpl implements PlanController {

	Logger LOGGER = LoggerFactory.getLogger(PlanControllerImpl.class);

	@Autowired
	PlanService service;

	@Override
	public ResponseStatus addPlan(PlanDTO plan) {
		return service.addPlan(plan);
	}

	@Override
	public ResponseStatus addPlans(List<PlanDTO> planList) {
		return service.addPlans(planList);
	}

	@Override
	public PlanDTO searchPlan(Long planId) {
		return service.searchPlan(planId);
	}

	@Override
	public PlanDTO searchPlan(String planName) {
		return service.searchPlan(planName);
	}

	@Override
	public List<PlanDTO> getAllPlans() {
		return service.getAllPlans();
	}

	@Override
	public ResponseStatus updatePlan(PlanDTO plan) {
		return service.updatePlan(plan);
		
	}

	@Override
	public ResponseStatus deletePlan(Long planId) {
		return service.deletePlan(planId);
	}

	@Override
	public ResponseStatus deletePlan(String planName) {
		return service.deletePlan(planName);
	}

	@Override
	public ResponseStatus addBasicPlans() {
		return service.addBasicPlans();
	}

}
