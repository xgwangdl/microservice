package com.accenture.common.exception;

public class RestException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8148263925714955642L;

	private String code;
	private String message;
	
	
	public String getCode() {
		return code;
	}


	public String getMessage() {
		return message;
	}


	public RestException(String code,String message) {
		this.code = code;
		this.message = message;
	}
}
