package com.thetransactioncompany.cors;


import junit.framework.TestCase;


/**
 * Tests the header field name class.
 *
 * @author Vladimir Dzhuvinov
 */
public class HeaderNameTest extends TestCase {


	public void testConstants() {

		assertEquals("Origin", HeaderName.ORIGIN);
		assertEquals("Access-Control-Request-Method", HeaderName.ACCESS_CONTROL_REQUEST_METHOD);
		assertEquals("Access-Control-Request-Headers", HeaderName.ACCESS_CONTROL_REQUEST_HEADERS);
		assertEquals("Access-Control-Allow-Origin", HeaderName.ACCESS_CONTROL_ALLOW_ORIGIN);
		assertEquals("Access-Control-Allow-Methods", HeaderName.ACCESS_CONTROL_ALLOW_METHODS);
		assertEquals("Access-Control-Allow-Headers", HeaderName.ACCESS_CONTROL_ALLOW_HEADERS);
		assertEquals("Access-Control-Allow-Credentials", HeaderName.ACCESS_CONTROL_ALLOW_CREDENTIALS);
		assertEquals("Access-Control-Max-Age", HeaderName.ACCESS_CONTROL_MAX_AGE);
		assertEquals("Access-Control-Expose-Headers", HeaderName.ACCESS_CONTROL_EXPOSE_HEADERS);
		assertEquals("Access-Control-Expose-Headers", HeaderName.ACCESS_CONTROL_EXPOSE_HEADERS);
		assertEquals("Vary", HeaderName.VARY);
		assertEquals("Host", HeaderName.HOST);
	}


	public void testFormatCanonical1() {

		assertEquals(HeaderName.formatCanonical("content-type"), "Content-Type");
	}


	public void testFormatCanonical2() {

		assertEquals(HeaderName.formatCanonical("CONTENT-TYPE"), "Content-Type");
	}


	public void testFormatCanonical3() {

		assertEquals(HeaderName.formatCanonical("X-type"), "X-Type");
	}


	public void testFormatCanonical4() {

		assertEquals(HeaderName.formatCanonical("Origin"), "Origin");
	}


	public void testFormatCanonical5() {

		assertEquals(HeaderName.formatCanonical("A"), "A");
	}


	public void testFormatCanonical6() {

		try {
			assertEquals(HeaderName.formatCanonical(""), "");
			fail("Failed to raise IllegalArgumentException on empty string");

		} catch (IllegalArgumentException e) {
			// ok
		}
	}


	public void testTrim() {
		String expected = "Content-Type";
		String n1 = HeaderName.formatCanonical("content-type\n");
		String n2 = HeaderName.formatCanonical(" CONTEnt-Type ");

		assertEquals("All whitespace should be trimmed", expected, n1);
		assertEquals("All whitespace should be trimmed", expected, n2);
	}


	public void testInvalid1() {
		assertInvalid("X-r@b");
	}


	public void testInvalid2() {
		assertInvalid("1=X-r");
	}


	public void testInvalid3() {
		assertInvalid("Aaa Bbb");
	}


	public void testInvalid4() {
		assertInvalid("less<than");
	}


	public void testInvalid5() {
		assertInvalid("alpha1>");
	}


	public void testInvalid6() {
		assertInvalid("X-Forwarded-By-{");
	}


	public void testInvalid7() {
		assertInvalid("a}");
	}


	public void testInvalid8() {
		assertInvalid("separator:");
	}


	public void testInvalid9() {
		assertInvalid("asd\"f;");
	}


	public void testInvalid10() {
		assertInvalid("rfc@w3c.org");
	}


	public void testInvalid11() {
		assertInvalid("bracket[");
	}


	public void testInvalid12() {
		assertInvalid("control\u0002header");
	}


	public void testInvalid13() {
		assertInvalid("control\nembedded");
	}


	public void testInvalid14() {
		assertInvalid("uni╚(•⌂•)╝");
	}


	public void testInvalid15() {
		assertInvalid("uni\u3232_\u3232");
	}


	public void testUnusualButValid() {
		HeaderName.formatCanonical("__2");
		HeaderName.formatCanonical("$%.%");
		HeaderName.formatCanonical("`~'&#*!^|");
		HeaderName.formatCanonical("Original_Name");
	}


	private void assertInvalid(String header) {
		try {
			HeaderName.formatCanonical(header);

			fail("Failed to raise exeption on bad header name");

		} catch (IllegalArgumentException e) {
			// ok
		}

	}

}
