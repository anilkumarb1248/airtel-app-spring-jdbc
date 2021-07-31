package com.app.airtel.exceptions;

public class PlanNotFoundException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public PlanNotFoundException(String message) {
		super(message);
	}

}
