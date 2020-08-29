package com.thetransactioncompany.cors;


/**
 * Origin exception.
 *
 * @author Vladimir Dzhuvinov
 */
public class OriginException extends Exception {


	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Creates a new origin exception with the specified message.
	 *
	 * @param message The message.
	 */
	public OriginException(final String message) {

		super(message);
	}
}