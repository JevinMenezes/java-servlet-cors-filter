package com.thetransactioncompany.cors;


import java.util.Properties;

import junit.framework.TestCase;


/**
 * Tests the CORS configuration class.
 *
 * @author Vladimir Dzhuvinov
 */
public class CorsConfigurationTest extends TestCase {
	
	
	public void testParseWordsSpace() {
	
		String[] p1 = CorsConfiguration.parseWords("GET POST HEAD");
		
		assertEquals(3, p1.length);
	}
	
	
	public void testParseWordsComma() {
	
		String[] p1 = CorsConfiguration.parseWords("GET,POST,HEAD");
		
		assertEquals(3, p1.length);
	}
	
	
	public void testParseWordsMixed1() {
	
		String[] p1 = CorsConfiguration.parseWords("GET, POST, HEAD");
		
		assertEquals(3, p1.length);
	}
	
	
	public void testParseWordsMixed2() {
	
		String[] p1 = CorsConfiguration.parseWords("GET , POST , HEAD");
		
		assertEquals(3, p1.length);
	}
	
	
	public void testParseWordsEmpty() {
	
		String[] p1 = CorsConfiguration.parseWords("");
		
		assertEquals(0, p1.length);
	}
	
	
	public void testDefaultConfig() {
        
		Properties p = new Properties();
		
		CorsConfiguration c = null;
		
		try {
			c = new CorsConfiguration(p);
		
		} catch (CorsConfigurationException e) {
			fail(e.getMessage());
		}
		
		assertTrue(c.allowGenericHttpRequests);
		
		assertTrue(c.allowAnyOrigin);
		assertTrue(c.isAllowedOrigin(new Origin("http://example.com")));
		
		assertTrue(c.isSupportedMethod("GET"));
		assertTrue(c.isSupportedMethod("POST"));
		assertTrue(c.isSupportedMethod("HEAD"));
		assertTrue(c.isSupportedMethod("OPTIONS"));
		assertFalse(c.isSupportedMethod("DELETE"));
		assertFalse(c.isSupportedMethod("PUT"));
		assertFalse(c.isSupportedMethod("TRACE"));
		
		assertTrue(c.supportAnyHeader);
		assertTrue(c.isSupportedHeader("X-Requested-By"));
		
		assertEquals(-1, c.maxAge);

		assertFalse(c.tagRequests);
        }
	
	
	public void testDefaultConfigWithSystemPropertyOverride() {
		
		System.setProperty("cors.allowGenericHttpRequests", "false");
  
		Properties p = new Properties();
		
		CorsConfiguration c = null;
		
		try {
			c = new CorsConfiguration(p);
		
		} catch (CorsConfigurationException e) {
			fail(e.getMessage());
		}
		
		assertFalse(c.allowGenericHttpRequests);
		
		assertTrue(c.allowAnyOrigin);
		assertTrue(c.isAllowedOrigin(new Origin("http://example.com")));
		
		assertTrue(c.isSupportedMethod("GET"));
		assertTrue(c.isSupportedMethod("POST"));
		assertTrue(c.isSupportedMethod("HEAD"));
		assertTrue(c.isSupportedMethod("OPTIONS"));
		assertFalse(c.isSupportedMethod("DELETE"));
		assertFalse(c.isSupportedMethod("PUT"));
		assertFalse(c.isSupportedMethod("TRACE"));
		
		assertTrue(c.supportAnyHeader);
		assertTrue(c.isSupportedHeader("X-Requested-By"));
		
		assertEquals(-1, c.maxAge);

		assertFalse(c.tagRequests);
		
		System.clearProperty("cors.allowGenericHttpRequests");
        }
        
        
        public void testPublicConfig() {
        
		Properties p = new Properties();
		p.setProperty("cors.allowGenericHttpRequests", "true");
		p.setProperty("cors.allowOrigin", "*");
		p.setProperty("cors.supportedMethods", "GET, POST, OPTIONS");
		p.setProperty("cors.supportedHeaders", "*");
		p.setProperty("cors.supportsCredentials", "false");
		p.setProperty("cors.tagRequests", "true");
		
		CorsConfiguration c = null;
		
		try {
			c = new CorsConfiguration(p);
		
		} catch (CorsConfigurationException e) {
			fail(e.getMessage());
		}
		
		assertTrue(c.allowGenericHttpRequests);
		
		assertTrue(c.allowAnyOrigin);		
		assertTrue(c.isAllowedOrigin(new Origin("http://example.com")));
		
		assertTrue(c.isSupportedMethod("GET"));
		assertTrue(c.isSupportedMethod("POST"));
		assertTrue(c.isSupportedMethod("OPTIONS"));
		assertFalse(c.isSupportedMethod("DELETE"));
		assertFalse(c.isSupportedMethod("PUT"));
		assertFalse(c.isSupportedMethod("TRACE"));

		assertTrue(c.supportAnyHeader);
		assertTrue(c.isSupportedHeader("X-Requested-By"));

		assertTrue(c.tagRequests);
        }
	
	
	public void testRestrictedConfig() {
        
		Properties p = new Properties();
		p.setProperty("cors.allowGenericHttpRequests", "false");
		p.setProperty("cors.allowOrigin", "http://example.com:8080");
		p.setProperty("cors.supportedMethods", "GET, POST, OPTIONS");
		p.setProperty("cors.supportedHeaders", "");
		p.setProperty("cors.supportsCredentials", "false");
		p.setProperty("cors.tagRequests", "false");
		
		CorsConfiguration c = null;
		
		try {
			c = new CorsConfiguration(p);
		
		} catch (CorsConfigurationException e) {
			fail(e.getMessage());
		}
		
		assertFalse(c.allowGenericHttpRequests);
		
		assertFalse(c.allowAnyOrigin);
		assertTrue(c.isAllowedOrigin(new Origin("http://example.com:8080")));
		assertFalse(c.isAllowedOrigin(new Origin("http://example.com:8008")));
		assertFalse(c.isAllowedOrigin(new Origin("http://example.com")));
		assertFalse(c.isAllowedOrigin(new Origin("http://deny-origin.com")));

		assertFalse(c.supportAnyHeader);
		assertFalse(c.isSupportedHeader("X-Requested-By"));

		assertFalse(c.tagRequests);
        }
	
	
	public void testCustomHeaders() {
	
		String h1 = "X-Requested-By";
		String h2 = "X-Web-Client";
		String h3 = "X-Not-Included";
	
		Properties p = new Properties();
		p.setProperty("cors.supportedHeaders", h1 + " " + h2);
		
		
		CorsConfiguration c = null;
		
		try {
			c = new CorsConfiguration(p);
		
		} catch (CorsConfigurationException e) {
			fail(e.getMessage());
		}
		
		assertTrue(c.isSupportedHeader(h1));
		assertTrue(c.isSupportedHeader(h2));
		assertFalse(c.isSupportedHeader(h3));
	}
	
	
	public void testExposedHeaders() {
	
		String h1 = "X-Powered-By";
		String h2 = "X-Web-Service";
		String h3 = "X-Hidden";
	
		Properties p = new Properties();
		p.setProperty("cors.exposedHeaders", h1 + " " + h2);
		
		
		CorsConfiguration c = null;
		
		try {
			c = new CorsConfiguration(p);
		
		} catch (CorsConfigurationException e) {
			fail(e.getMessage());
		}
		
		assertTrue(c.exposedHeaders.contains(h1));
		assertTrue(c.exposedHeaders.contains(h2));
		assertFalse(c.exposedHeaders.contains(h3));
	}
	
	
	public void testSupportsCredentialsTrue() {
	
		Properties p = new Properties();
		p.setProperty("cors.supportsCredentials", "true");
		
		
		CorsConfiguration c = null;
		
		try {
			c = new CorsConfiguration(p);
		
		} catch (CorsConfigurationException e) {
			fail(e.getMessage());
		}
		
		assertTrue(c.supportsCredentials);
	
	}
	
	
	public void testSupportsCredentialsFalse() {
	
		Properties p = new Properties();
		p.setProperty("cors.supportsCredentials", "false");
		
		
		CorsConfiguration c = null;
		
		try {
			c = new CorsConfiguration(p);
		
		} catch (CorsConfigurationException e) {
			fail(e.getMessage());
		}
		
		assertFalse(c.supportsCredentials);
	
	}
	
	
	public void testMaxAge() {
	
		Properties p = new Properties();
		p.setProperty("cors.maxAge", "100");
		
		
		CorsConfiguration c = null;
		
		try {
			c = new CorsConfiguration(p);
		
		} catch (CorsConfigurationException e) {
			fail(e.getMessage());
		}
		
		assertEquals(100, c.maxAge);
	
	}
	
	
	public void testDenySubdomainOrigins(){
	    
		Origin origin = new Origin("http://example.com:8080");
		Origin subdomainOrigin = new Origin("http://test.example.com:8080");

		Properties p = new Properties();
		p.setProperty("cors.allowOrigin", origin.toString());

		CorsConfiguration c = null;

		try {

			c = new CorsConfiguration(p);

		} catch (CorsConfigurationException e) {
		
			fail(e.getMessage());
		}

		assertFalse(c.allowSubdomains);
		assertTrue(c.isAllowedSubdomainOrigin(subdomainOrigin));
		assertTrue(c.isAllowedOrigin(origin));
		assertFalse(c.isAllowedOrigin(subdomainOrigin));
	}
	

	public void testAllowSubdomainOrigin(){

		Properties p = new Properties();
		p.setProperty("cors.allowSubdomains", "true");
		p.setProperty("cors.allowOrigin", "http://example.com:8080");

		CorsConfiguration c = null;

		try {
			c = new CorsConfiguration(p);

		} catch (CorsConfigurationException e) {

			fail(e.getMessage());
		}

		Origin origin = new Origin("http://test.example.com:8080");

		assertTrue(c.isAllowedSubdomainOrigin(origin));
		assertTrue(c.isAllowedOrigin(origin));
		
		
		origin = new Origin("http://myexample.com:8080");

		assertFalse(c.isAllowedSubdomainOrigin(origin));
		assertFalse(c.isAllowedOrigin(origin));
	}
}
