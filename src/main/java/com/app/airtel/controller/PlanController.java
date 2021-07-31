package com.app.airtel.controller;

import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.app.airtel.dto.PlanDTO;
import com.app.airtel.util.ResponseStatus;

@RequestMapping("/plan")
public interface PlanController {

	@PostMapping("/add")
	public ResponseStatus addPlan(@RequestBody PlanDTO plan);

	@PostMapping("/addAll")
	public ResponseStatus addPlans(@RequestBody List<PlanDTO> planList);

	@GetMapping("/search/{planId}")
	public PlanDTO searchPlan(@PathVariable(value = "planId") Long planId);

	@GetMapping("/searchByName")
	public PlanDTO searchPlan(@RequestParam(value = "planName") String planName);

	@GetMapping("/getAll")
	public List<PlanDTO> getAllPlans();

	@PutMapping("/update")
	public ResponseStatus updatePlan(@RequestBody PlanDTO plan);

	@DeleteMapping("/delete/{planId}")
	public ResponseStatus deletePlan(@PathVariable(value = "planId") Long planId);

	@DeleteMapping("/deleteByName")
	public ResponseStatus deletePlan(@RequestParam(value = "planName") String planName);

	@PostMapping("/addBasicPlans")
	public ResponseStatus addBasicPlans();
}
