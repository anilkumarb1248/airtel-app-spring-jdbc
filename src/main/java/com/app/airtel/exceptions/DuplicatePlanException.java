package com.app.airtel.exceptions;

public class DuplicatePlanException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public DuplicatePlanException(String message) {
		super(message);
	}

}
