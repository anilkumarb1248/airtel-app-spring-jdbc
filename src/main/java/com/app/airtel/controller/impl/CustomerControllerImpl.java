package com.app.airtel.controller.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import com.app.airtel.controller.CustomerController;
import com.app.airtel.dto.CustomerDTO;
import com.app.airtel.service.CustomerService;
import com.app.airtel.util.ResponseStatus;

@RestController
public class CustomerControllerImpl implements CustomerController {

	@Autowired
	CustomerService service;

	@Override
	public ResponseStatus addCustomer(CustomerDTO customer) {
		return service.addCustomer(customer);
	}

	@Override
	public ResponseStatus addCustomers(List<CustomerDTO> customerList) {
		return service.addCustomers(customerList);
	}

	@Override
	public CustomerDTO searchCustomer(Long phoneNumber) {
		return service.searchCustomer(phoneNumber);
	}

	@Override
	public CustomerDTO searchCustomer(String name) {
		return service.searchCustomer(name);
	}

	@Override
	public List<CustomerDTO> getAllCustomers() {
		return service.getAllCustomers();
	}

	@Override
	public ResponseStatus updateCustomer(CustomerDTO customer) {
		return service.updateCustomer(customer);
	}

	@Override
	public ResponseStatus deleteCustomer(Long phoneNumber) {
		return service.deleteCustomer(phoneNumber);
	}

	@Override
	public ResponseStatus deleteCustomer(String name) {
		return service.deleteCustomer(name);
	}

}
