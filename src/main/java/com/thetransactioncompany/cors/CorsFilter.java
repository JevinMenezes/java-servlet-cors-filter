package com.thetransactioncompany.cors;


import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
 * Cross-Origin Resource Sharing (CORS) servlet filter.
 *
 * <p>The filter intercepts incoming HTTP requests and applies the CORS
 * policy as specified by the filter init parameters. The actual CORS
 * request is processed by the {@link CorsRequestHandler} class.
 *
 * <p>Supported configuration parameters:
 *
 * <ul>
 *     <li>cors.allowGenericHttpRequests {true|false} defaults to {@code true}.
 *     <li>cors.allowOrigin {"*"|origin-list} defaults to {@code *}.
 *     <li>cors.allowSubdomains {true|false} defaults to {@code false}.
 *     <li>cors.supportedMethods {method-list} defaults to {@code "GET, POST,
 *         HEAD, OPTIONS"}.
 *     <li>cors.supportedHeaders {"*"|header-list} defaults to {@code *}.
 *     <li>cors.exposedHeaders {header-list} defaults to empty list.
 *     <li>cors.supportsCredentials {true|false} defaults to {@code true}.
 *     <li>cors.maxAge {int} defaults to {@code -1} (unspecified).
 *     <li>cors.tagRequests {boolean} default to {@code false}.
 * </ul>
 *
 * @author Vladimir Dzhuvinov
 * @author David Bellem
 * @author Gervasio Amy
 * @author Mike Holdsworth
 */
public class CorsFilter implements Filter {


	/**
	 * The CORS filer configuration.
	 */
	private CorsConfiguration config;


	/**
	 * Encapsulates the CORS request handling logic.
	 */
	private CorsRequestHandler handler;


	/**
	 * Creates a new uninitialised CORS filter. Must be then initialised
	 * with {@link #setConfiguration} or {@link #init}.
	 */
	public CorsFilter() {

		super();
	}


	/**
	 * Allows an alternative means of setting the configuration that suits
	 * a Spring bean constructor approach rather than a servlet init
	 * approach.
	 *
	 * See https://bitbucket.org/thetransactioncompany/cors-filter/issue/24
	 *
	 * @param config The cross-origin access policy. Must not be
	 *               {@code null}.
	 */
	public CorsFilter(final CorsConfiguration config) {

		setConfiguration(config);
	}


	/**
	 * Sets the cross-origin access policy for this CORS filter.
	 *
	 * @param config The cross-origin access policy. Must not be
	 *               {@code null}.
	 */
	public void setConfiguration(final CorsConfiguration config) {

		this.config = config;
		handler = new CorsRequestHandler(config);
	}


	/**
	 * Gets the cross-origin access policy for this CORS filter.
	 *
	 * @return The cross-origin access policy, {@code null} if the filter
	 *         is not initialised.
	 */
	public CorsConfiguration getConfiguration() {

		return config;
	}


	/**
	 * This method is invoked by the servlet container to initialise the
	 * filter at startup.
	 *
	 * @param filterConfig The servlet filter configuration. Must not be
	 *                     {@code null}.
	 *
	 * @throws ServletException On a filter initialisation exception.
	 */
	@Override
	public void init(final FilterConfig filterConfig)
		throws ServletException {

		CorsConfigurationLoader configLoader = new CorsConfigurationLoader(filterConfig);

		try {
			setConfiguration(configLoader.load());

		} catch (CorsConfigurationException e) {

			throw new ServletException(e.getMessage(), e);
		}
	}


	/**
	 * Produces a simple HTTP text/plain response for the specified CORS
	 * exception.
	 *
	 * <p>Note: The CORS filter avoids falling back to the default web
	 * container error page (typically a richly-formatted HTML page) to
	 * make it easier for XHR debugger tools to identify the cause of
	 * failed requests.
	 *
	 * @param corsException The CORS exception. Must not be {@code null}.
	 * @param response      The HTTP servlet response. Must not be
	 *                      {@code null}.
	 *
	 * @throws IOException      On a I/O exception.
	 * @throws ServletException On a general request processing exception.
	 */
	private void printMessage(final CorsException corsException,
				  final HttpServletResponse response)
		throws IOException, ServletException {

		// Set the status code
		response.setStatus(corsException.getHTTPStatusCode());

		// Write the error message
		response.resetBuffer();
		response.setContentType("text/plain");
		PrintWriter out = response.getWriter();
		out.println("Cross-Origin Resource Sharing (CORS) Filter: " + corsException.getMessage());
	}


	/**
	 * Filters an HTTP request / response pair according to the configured
	 * CORS policy. Also tags the request with CORS information to
	 * downstream handlers.
	 *
	 * @param request  The servlet request.
	 * @param response The servlet response.
	 * @param chain    The servlet filter chain.
	 *
	 * @throws IOException      On a I/O exception.
	 * @throws ServletException On a general request processing exception.
	 */
	private void doFilter(final HttpServletRequest request,
		              final HttpServletResponse response,
		              final FilterChain chain)
		throws IOException, ServletException {

			CorsRequestType type = CorsRequestType.detect(request);

		// Tag if configured
		if (config.tagRequests)
			RequestTagger.tag(request, type);

		try {
			if (type.equals(CorsRequestType.ACTUAL)) {

				// Simple / actual CORS request
				handler.handleActualRequest(request, response);

				// Preserve CORS response headers on reset()
				CorsResponseWrapper responseWrapper = new CorsResponseWrapper(response);

				chain.doFilter(request, responseWrapper);

			} else if (type.equals(CorsRequestType.PREFLIGHT)) {

				// Preflight CORS request, handle but don't pass
				// further down the chain
				handler.handlePreflightRequest(request, response);

			} else if (config.allowGenericHttpRequests) {

				// Not a CORS request, but allow it through
				chain.doFilter(request, response);

			} else {

				// Generic HTTP requests denied
				printMessage(CorsException.GENERIC_HTTP_NOT_ALLOWED, response);
			}
		} catch (CorsException e) {

			printMessage(e, response);
		}
	}


	/**
	 * Called by the servlet container each time a request / response pair
	 * is passed through the chain due to a client request for a resource
	 * at the end of the chain.
	 *
	 * @param request  The servlet request.
	 * @param response The servlet response.
	 * @param chain    The servlet filter chain.
	 *
	 * @throws IOException      On a I/O exception.
	 * @throws ServletException On a general request processing exception.
	 */
	@Override
	public void doFilter(final ServletRequest request,
		             final ServletResponse response,
		             final FilterChain chain)
		throws IOException, ServletException {

		if (request instanceof HttpServletRequest && response instanceof HttpServletResponse) {

			// Cast to HTTP
			doFilter((HttpServletRequest)request, (HttpServletResponse)response, chain);

		} else {

			throw new ServletException("Cannot filter non-HTTP requests/responses");
		}
	}


	/**
	 * Called by the web container to indicate to a filter that it is being
	 * taken out of service.
	 */
	@Override
	public void destroy() {

		// do nothing
	}
}
