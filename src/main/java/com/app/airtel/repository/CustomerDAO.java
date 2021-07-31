package com.app.airtel.repository;

import java.util.List;

import com.app.airtel.entity.Customer;

public interface CustomerDAO {

	public void addCustomer(Customer customer);

	public void addCustomers(List<Customer> customerList);

	public Customer searchCustomer(Long phoneNumber);

	public Customer searchCustomer(String name);

	public List<Customer> getAllCustomers();

	public void updateCustomer(Long phoneNumber, Customer customer);

	public void deleteCustomer(Long phoneNumber);

	public void deleteCustomer(String name);

}
