package com.app.airtel.exceptions;

public class AirTelDataAccessException extends RuntimeException {

	private static final long serialVersionUID = 4482265318380681563L;

	public AirTelDataAccessException(String message) {
		super(message);
	}

	public AirTelDataAccessException(String message, Throwable cause) {
		super(message, cause);
	}
}
