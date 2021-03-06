package com.thetransactioncompany.cors;


/**
 * CORS filter configuration exception, intended to report invalid init 
 * parameters at startup.
 *
 * @author Vladimir Dzhuvinov
 */
public class CorsConfigurationException extends Exception {


	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Creates a new CORS filter configuration exception with the specified message.
	 * 
	 * @param message The exception message.
	 */
	public CorsConfigurationException(final String message) {
	
		super(message);
	}


	/**
	 * Creates a new CORS filter configuration exception with the specified
	 * message and cause.
	 *
	 * @param message The exception message.
	 * @param cause   The exception cause.
	 */
	public CorsConfigurationException(final String message, final Throwable cause) {

		super(message, cause);
	}
}
