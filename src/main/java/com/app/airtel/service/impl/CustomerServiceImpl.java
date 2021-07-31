package com.app.airtel.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import com.app.airtel.dto.CustomerDTO;
import com.app.airtel.entity.Customer;
import com.app.airtel.exceptions.AirTelException;
import com.app.airtel.exceptions.CustomerNotFoundException;
import com.app.airtel.exceptions.DuplicateCustomerException;
import com.app.airtel.repository.CustomerDAO;
import com.app.airtel.service.CustomerService;
import com.app.airtel.util.ResponseStatus;

@Service
public class CustomerServiceImpl implements CustomerService {

	private static final Logger LOGGER = LoggerFactory.getLogger(CustomerServiceImpl.class);

	@Autowired
	CustomerDAO customerDAO;

	@Override
	public ResponseStatus addCustomer(CustomerDTO customerDTO) {
		if (!isDuplicateCustomer(true, customerDTO)) {
			try {
				customerDAO.addCustomer(prepareCustomerEntity(customerDTO));
				return prepareResponseStatus(HttpStatus.CREATED, "Customer added successfully");
			} catch (DataAccessException ex) {
				LOGGER.error("Exception occurred while adding the customer in CustomerServiceImpl - addCustomer(): {} ",
						ex);
				return prepareResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR,
						"Failed to add Customer: " + ex.getMessage());
			}
		} else {
			throw new DuplicateCustomerException("Customer already exist with name: " + customerDTO.getName());
		}
	}

	@Override
	public ResponseStatus addCustomers(List<CustomerDTO> customerDTOList) {
		Assert.notEmpty(customerDTOList, "Customer list is empty");
		List<Customer> customerList = new ArrayList<>();
		customerDTOList.stream().forEach(customerDTO -> {
			if (!isDuplicateCustomer(true, customerDTO)) {
				customerList.add(prepareCustomerEntity(customerDTO));
			}
		});
		try {
			if (!CollectionUtils.isEmpty(customerList)) {
				customerDAO.addCustomers(customerList);
			}
		} catch (DataAccessException ex) {
			LOGGER.error("Excepiton occured while adding the list of customers in CustomerServiceImpl: {}",
					ex.getMessage());
			return prepareResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR,
					"Error occured while adding the customers: " + ex.getMessage());
		}
		return prepareResponseStatus(HttpStatus.CREATED, "List of Customers added successfully");
	}

	@Override
	public CustomerDTO searchCustomer(Long phoneNumber) {
		try {
			Customer customer = customerDAO.searchCustomer(phoneNumber);
			return prepareCustomerDTO(customer);
		} catch (EmptyResultDataAccessException ex) {
			throw new CustomerNotFoundException("No customer found with phone number: " + phoneNumber);
		} catch (DataAccessException ex) {
			throw new AirTelException("Internal server error occured while fetching the customer: " + ex.getMessage());
		}
	}

	@Override
	public CustomerDTO searchCustomer(String name) {
		try {
			Customer customer = customerDAO.searchCustomer(name);
			return prepareCustomerDTO(customer);
		} catch (EmptyResultDataAccessException ex) {
			throw new CustomerNotFoundException("No customer found with customer name: " + name);
		} catch (DataAccessException ex) {
			throw new AirTelException("Internal server error occured while fetching the customer: " + ex.getMessage());
		}
	}

	@Override
	public List<CustomerDTO> getAllCustomers() {
		List<Customer> customerList = customerDAO.getAllCustomers();
		List<CustomerDTO> customrDTOList = new ArrayList<>();
		customerList.stream().forEach(customer -> {
			customrDTOList.add(prepareCustomerDTO(customer));
		});
		return customrDTOList;
	}

	@Override
	public ResponseStatus updateCustomer(CustomerDTO customerDTO) {
		if (!isDuplicateCustomer(false, customerDTO)) {
			try {
				customerDAO.updateCustomer(customerDTO.getPhoneNumber(), prepareCustomerEntity(customerDTO));
				return prepareResponseStatus(HttpStatus.OK, "Customer updated successfully");
			} catch (DataAccessException ex) {
				throw new AirTelException("Exception occured while updating the Customer: " + ex.getMessage());
			}
		} else {
			throw new DuplicateCustomerException("Customer already exist with name: " + customerDTO.getName());
		}
	}

	@Override
	public ResponseStatus deleteCustomer(Long phoneNumber) {
		try {
			customerDAO.deleteCustomer(phoneNumber);
			return prepareResponseStatus(HttpStatus.OK, "Customer deleted successfully");
		} catch (DataAccessException ex) {
			throw new AirTelException(
					"Exception occured while deleting the customer with phone number: " + phoneNumber);
		}
	}

	@Override
	public ResponseStatus deleteCustomer(String name) {
		try {
			customerDAO.deleteCustomer(name);
			return prepareResponseStatus(HttpStatus.OK, "Customer deleted successfully");
		} catch (DataAccessException ex) {
			throw new AirTelException("Exception occured while deleting the customer with name: " + name);
		}
	}

	private CustomerDTO prepareCustomerDTO(Customer customer) {
		CustomerDTO customerDTO = new CustomerDTO();
		BeanUtils.copyProperties(customer, customerDTO);
		return customerDTO;
	}

	private Customer prepareCustomerEntity(CustomerDTO customerDTO) {
		Customer customer = new Customer();
		BeanUtils.copyProperties(customerDTO, customer);
		return customer;
	}

	private ResponseStatus prepareResponseStatus(HttpStatus httpStatus, String message) {
		ResponseStatus responseStatus = new ResponseStatus();
		responseStatus.setStatusCode(String.valueOf(httpStatus.value()));
		responseStatus.setMessage(message);
		return responseStatus;
	}

	private boolean isDuplicateCustomer(boolean newFlag, CustomerDTO customerDTO) {
		try {
			Customer customer = customerDAO.searchCustomer(customerDTO.getName());
			if (newFlag || customer.getPhoneNumber() != customerDTO.getPhoneNumber()) {
				return true;
			} else {
				return false;
			}
		} catch (EmptyResultDataAccessException ex) {
			return false;
		} catch (Exception ex) {
			throw new AirTelException("Exception occured while checking duplicates");
		}
	}
}
