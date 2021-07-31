package com.app.airtel.util;

import java.io.Serializable;

public class ResponseStatus implements Serializable {

	private static final long serialVersionUID = 1L;

	private String statusCode;
	private String message;

	public ResponseStatus() {
	}

	public ResponseStatus(String statusCode, String message) {
		this.statusCode = statusCode;
		this.message = message;
	}

	public String getStatusCode() {
		return statusCode;
	}

	public void setStatusCode(String statusCode) {
		this.statusCode = statusCode;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
}
