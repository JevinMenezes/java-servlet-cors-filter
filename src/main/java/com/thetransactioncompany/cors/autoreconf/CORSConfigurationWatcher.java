package com.thetransactioncompany.cors.autoreconf;


/**
 * Interface for watching a
 * {@link com.thetransactioncompany.cors.CorsConfiguration CORS filter
 * configuration} file for changes.
 *
 * @author Aleksey Zvolinsky
 */
public interface CorsConfigurationWatcher {


	/**
	 * Start watching for changes.
	 */
	void start();


	/**
	 * Stop watching for changes.
	 */
	void stop();


	/**
	 * Checks if the CORS filter configuration has changed and must be
	 * reloaded.
	 *
	 * @return {@code true} if the CORS filter configuration must be
	 *         reloaded.
	 */
	boolean reloadRequired();


	/**
	 * Resets watching.
	 */
	void reset();
}
