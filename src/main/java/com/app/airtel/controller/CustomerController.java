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

import com.app.airtel.dto.CustomerDTO;
import com.app.airtel.util.ResponseStatus;

@RequestMapping("/customer")
public interface CustomerController {

	@PostMapping("/add")
	public ResponseStatus addCustomer(@RequestBody CustomerDTO customer);

	@PostMapping("/addAll")
	public ResponseStatus addCustomers(@RequestBody List<CustomerDTO> customerList);

	@GetMapping("/search/{phoneNo}")
	public CustomerDTO searchCustomer(@PathVariable(value = "phoneNo") Long phoneNumber);

	@GetMapping("/searchByName")
	public CustomerDTO searchCustomer(@RequestParam(value = "custName") String name);

	@GetMapping("/getAll")
	public List<CustomerDTO> getAllCustomers();

	@PutMapping("/update")
	public ResponseStatus updateCustomer(@RequestBody CustomerDTO customer);

	@DeleteMapping("/delete/{phoneNumber}")
	public ResponseStatus deleteCustomer(@PathVariable Long phoneNumber);

	@DeleteMapping("/deleteByName")
	public ResponseStatus deleteCustomer(@RequestParam(value = "custName") String name);

}
