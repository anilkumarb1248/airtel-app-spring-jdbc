package com.app.airtel.repository;

import java.util.List;
import java.util.Optional;

import com.app.airtel.entity.Plan;

public interface PlanDao {

	public void addPlan(Plan plan);

	public void addPlans(List<Plan> planList);

	public Optional<Plan> searchPlan(Long planId);

	public Optional<Plan> searchPlan(String planName);

	public List<Plan> getAllPlans();

	public void updatePlan(Long planId, Plan plan);

	public void deletePlan(Long planId);

	public void deletePlan(String planName);
	
	public boolean checkPlanNameExist(String planName);

}
