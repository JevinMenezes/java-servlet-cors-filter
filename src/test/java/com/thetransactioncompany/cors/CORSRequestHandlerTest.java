package com.thetransactioncompany.cors;


import java.util.Arrays;
import java.util.HashSet;
import java.util.Properties;
import java.util.Set;

import junit.framework.TestCase;


/**
 * Tests the CORS request handler.
 *
 * @author Vladimir Dzhuvinov
 */
public class CorsRequestHandlerTest extends TestCase {


	public void testActualRequestWithDefaultConfiguration()
		throws Exception {

		CorsConfiguration config = new CorsConfiguration(new Properties());

		CorsRequestHandler handler = new CorsRequestHandler(config);

		MockServletRequest request = new MockServletRequest();
		request.setHeader("Origin", "http://example.com");

		MockServletResponse response = new MockServletResponse();

		handler.handleActualRequest(request, response);

		assertEquals("http://example.com", response.getHeader("Access-Control-Allow-Origin"));
		assertEquals("Origin", response.getHeader("Vary"));

		assertEquals("true", response.getHeader("Access-Control-Allow-Credentials"));

		assertEquals(3, response.getHeaders().size());
	}


	public void testActualRequestWithCredentialsNotAllowed()
		throws Exception {

		Properties props = new Properties();
		props.setProperty("cors.supportsCredentials", "false");
		CorsConfiguration config = new CorsConfiguration(props);

		CorsRequestHandler handler = new CorsRequestHandler(config);

		MockServletRequest request = new MockServletRequest();
		request.setHeader("Origin", "http://example.com");

		MockServletResponse response = new MockServletResponse();

		handler.handleActualRequest(request, response);

		assertEquals("*", response.getHeader("Access-Control-Allow-Origin"));

		assertNull(response.getHeader("Access-Control-Allow-Credentials"));

		assertEquals(1, response.getHeaders().size());
	}


	public void testActualRequestWithExposedHeaders()
		throws Exception {

		Properties props = new Properties();
		props.put("cors.exposedHeaders", "X-Custom");

		CorsConfiguration config = new CorsConfiguration(props);

		CorsRequestHandler handler = new CorsRequestHandler(config);

		MockServletRequest request = new MockServletRequest();
		request.setHeader("Origin", "http://example.com");

		MockServletResponse response = new MockServletResponse();

		handler.handleActualRequest(request, response);

		assertEquals("http://example.com", response.getHeader("Access-Control-Allow-Origin"));
		assertEquals("Origin", response.getHeader("Vary"));

		assertEquals("true", response.getHeader("Access-Control-Allow-Credentials"));

		assertEquals("X-Custom", response.getHeader("Access-Control-Expose-Headers"));

		assertEquals(4, response.getHeaders().size());
	}


	public void testActualRequestWithDeniedOrigin()
		throws Exception {

		Properties props = new Properties();
		props.put("cors.allowOrigin", "http://example.com");

		CorsConfiguration config = new CorsConfiguration(props);

		CorsRequestHandler handler = new CorsRequestHandler(config);

		MockServletRequest request = new MockServletRequest();
		request.setHeader("Origin", "http://other.com");

		MockServletResponse response = new MockServletResponse();

		try {
			handler.handleActualRequest(request, response);
			fail();
		} catch (CorsException e) {
			// ok
			assertEquals("CORS origin denied", e.getMessage());
			assertEquals(403, e.getHTTPStatusCode());
		}
	}


	public void testActualRequestWithUnsupportedMethod()
		throws Exception {

		Properties props = new Properties();
		props.put("cors.supportedMethods", "GET POST");

		CorsConfiguration config = new CorsConfiguration(props);

		CorsRequestHandler handler = new CorsRequestHandler(config);

		MockServletRequest request = new MockServletRequest();
		request.setHeader("Origin", "http://example.com");
		request.setMethod("DELETE");

		MockServletResponse response = new MockServletResponse();

		try {
			handler.handleActualRequest(request, response);
			fail();
		} catch (CorsException e) {
			// ok
			assertEquals("Unsupported HTTP method", e.getMessage());
			assertEquals(405, e.getHTTPStatusCode());
		}
	}


	public void testPreflightRequestWithDefaultConfiguration()
		throws Exception {

		CorsConfiguration config = new CorsConfiguration(new Properties());

		CorsRequestHandler handler = new CorsRequestHandler(config);

		MockServletRequest request = new MockServletRequest();
		request.setHeader("Origin", "http://example.com");
		request.setHeader("Access-Control-Request-Method", "POST");
		request.setMethod("OPTIONS");

		MockServletResponse response = new MockServletResponse();

		handler.handlePreflightRequest(request, response);

		assertEquals("http://example.com", response.getHeader("Access-Control-Allow-Origin"));
		assertEquals("Origin", response.getHeader("Vary"));

		Set<String> methods = new HashSet<String>(Arrays.asList(HeaderUtils.parseMultipleHeaderValues(response.getHeader("Access-Control-Allow-Methods"))));
		assertTrue(methods.contains("HEAD"));
		assertTrue(methods.contains("GET"));
		assertTrue(methods.contains("POST"));
		assertTrue(methods.contains("OPTIONS"));
		assertEquals(4, methods.size());

		assertEquals("true", response.getHeader("Access-Control-Allow-Credentials"));

		assertEquals(4, response.getHeaders().size());
	}


	public void testPreflightRequestWithCredentialsNotAllowed()
		throws Exception {

		Properties props = new Properties();
		props.setProperty("cors.supportsCredentials", "false");
		CorsConfiguration config = new CorsConfiguration(props);

		CorsRequestHandler handler = new CorsRequestHandler(config);

		MockServletRequest request = new MockServletRequest();
		request.setHeader("Origin", "http://example.com");
		request.setHeader("Access-Control-Request-Method", "POST");
		request.setMethod("OPTIONS");

		MockServletResponse response = new MockServletResponse();

		handler.handlePreflightRequest(request, response);

		assertEquals("*", response.getHeader("Access-Control-Allow-Origin"));

		Set<String> methods = new HashSet<String>(Arrays.asList(HeaderUtils.parseMultipleHeaderValues(response.getHeader("Access-Control-Allow-Methods"))));
		assertTrue(methods.contains("HEAD"));
		assertTrue(methods.contains("GET"));
		assertTrue(methods.contains("POST"));
		assertTrue(methods.contains("OPTIONS"));
		assertEquals(4, methods.size());

		assertNull(response.getHeader("Access-Control-Allow-Credentials"));

		assertEquals(2, response.getHeaders().size());
	}


	public void testPreflightRequestWithSupportAnyHeader()
		throws Exception {

		Properties props = new Properties();
		props.setProperty("cors.supportedHeaders", "*");

		CorsConfiguration config = new CorsConfiguration(props);

		CorsRequestHandler handler = new CorsRequestHandler(config);

		MockServletRequest request = new MockServletRequest();
		request.setHeader("Origin", "http://example.com");
		request.setHeader("Access-Control-Request-Method", "POST");
		request.setHeader("Access-Control-Request-Headers", "Authorization, Content-Type");
		request.setMethod("OPTIONS");

		MockServletResponse response = new MockServletResponse();

		handler.handlePreflightRequest(request, response);

		assertEquals("Authorization, Content-Type", response.getHeader("Access-Control-Allow-Headers"));
	}
}
