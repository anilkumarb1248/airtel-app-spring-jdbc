package com.app.airtel.service;

import java.util.List;

import com.app.airtel.dto.CustomerDTO;
import com.app.airtel.util.ResponseStatus;

public interface CustomerService {

	public ResponseStatus addCustomer(CustomerDTO customerDTO);

	public ResponseStatus addCustomers(List<CustomerDTO> customerDTOList);

	public CustomerDTO searchCustomer(Long phoneNumber);

	public CustomerDTO searchCustomer(String name);

	public List<CustomerDTO> getAllCustomers();

	public ResponseStatus updateCustomer(CustomerDTO customerDTO);

	public ResponseStatus deleteCustomer(Long phoneNumber);

	public ResponseStatus deleteCustomer(String name);

}
