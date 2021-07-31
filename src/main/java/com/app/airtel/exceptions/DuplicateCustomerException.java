package com.app.airtel.exceptions;

public class DuplicateCustomerException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public DuplicateCustomerException(String message) {
		super(message);
	}

}
