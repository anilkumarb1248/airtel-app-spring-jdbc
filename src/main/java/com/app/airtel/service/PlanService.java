package com.app.airtel.service;

import java.util.List;

import com.app.airtel.dto.PlanDTO;
import com.app.airtel.util.ResponseStatus;

public interface PlanService {

	public ResponseStatus addPlan(PlanDTO planDTO);

	public ResponseStatus addPlans(List<PlanDTO> planDTOList);

	public PlanDTO searchPlan(Long planId);

	public PlanDTO searchPlan(String planName);

	public List<PlanDTO> getAllPlans();

	public ResponseStatus updatePlan(PlanDTO planDTO);

	public ResponseStatus deletePlan(Long planId);

	public ResponseStatus deletePlan(String planName);

	public ResponseStatus addBasicPlans();

}
