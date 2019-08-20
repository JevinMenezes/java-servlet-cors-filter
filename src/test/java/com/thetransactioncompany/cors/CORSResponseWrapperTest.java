package com.thetransactioncompany.cors;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Collection;
import java.util.Locale;
import java.util.Map;
import java.util.HashMap;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

import junit.framework.TestCase;

/**
 * Tests the CORS response wrapper.
 */
public class CORSResponseWrapperTest extends TestCase {

    public void testReset() {

        // GIVEN
        final String otherHeaderName = "X-Other-Header";

        final Map<String, String> headers = new HashMap<>();
		headers.put(HeaderName.ACCESS_CONTROL_ALLOW_CREDENTIALS, "allow-credentials");
		headers.put(HeaderName.ACCESS_CONTROL_ALLOW_ORIGIN, "allow-origin");
		headers.put(HeaderName.ACCESS_CONTROL_EXPOSE_HEADERS, "expose-headers");
		headers.put(HeaderName.VARY, "vary");
        headers.put(otherHeaderName, "other-header");

        final MockHttpServletResponse responseMock = new MockHttpServletResponse(headers);

        // WHEN
        final CORSResponseWrapper sut = new CORSResponseWrapper(responseMock);
        sut.reset();

        // THEN
        assertEquals("allow-credentials", headers.get(HeaderName.ACCESS_CONTROL_ALLOW_CREDENTIALS));
        assertEquals("allow-origin", headers.get(HeaderName.ACCESS_CONTROL_ALLOW_ORIGIN));
        assertEquals("expose-headers", headers.get(HeaderName.ACCESS_CONTROL_EXPOSE_HEADERS));
        assertEquals("vary", headers.get(HeaderName.VARY));
        assertFalse(headers.containsKey(otherHeaderName));

        assertTrue(responseMock.isReset());

        // Mockito.verify(responseMock).reset();
    }

    private static final class MockHttpServletResponse implements HttpServletResponse {

        private final Map<String, String> headers;
        private boolean reset;

        MockHttpServletResponse(final Map<String, String> headers) {

            this.headers = headers;
        }

        @Override
        public String getHeader(final String name) {

            return headers.get(name);
        }

        @Override
        public Collection<String> getHeaderNames() {

            return headers.keySet();
        }

        public boolean isReset() {

            return reset;
        }

        @Override
        public void reset() {

            headers.clear();
            reset = true;
        }

        @Override
        public void setHeader(final String name, final String value) {

            headers.put(name, value);
        }

        //
        // Not implemented HttpServletResponse

        @Override
        public void addCookie(final Cookie cookie) {
            
            throw new UnsupportedOperationException();
        }

        @Override
        public void addDateHeader(final String name, final long date) {

            throw new UnsupportedOperationException();
        }

        @Override
        public void addHeader(final String name, final String value) {

            throw new UnsupportedOperationException();
        }

        @Override
        public void addIntHeader(final String name, final int value) {

            throw new UnsupportedOperationException();
        }

        @Override
        public boolean containsHeader(final String name) {

            throw new UnsupportedOperationException();
        }

        @Override
        public String encodeRedirectUrl(final String url) {

            throw new UnsupportedOperationException();
        }

        @Override
        public String encodeRedirectURL(final String url) {

            throw new UnsupportedOperationException();
        }

        @Override
        public String encodeUrl(final String url) {

            throw new UnsupportedOperationException();
        }

        @Override
        public String encodeURL(final String url) {

            throw new UnsupportedOperationException();
        }

        @Override
        public Collection<String> getHeaders(final String name) {

            throw new UnsupportedOperationException();
        }

        @Override
        public int getStatus() {

            throw new UnsupportedOperationException();
        }

        @Override
        public void sendError(final int sc) throws IOException {

            throw new UnsupportedOperationException();
        }

        @Override
        public void sendError(final int sc, final String msg) throws IOException {

            throw new UnsupportedOperationException();
        }

        @Override
        public void sendRedirect(final String location) throws IOException {

            throw new UnsupportedOperationException();
        }

        @Override
        public void setDateHeader(final String name, final long date) {

            throw new UnsupportedOperationException();
        }

        @Override
        public void setIntHeader(final String name, final int value) {

            throw new UnsupportedOperationException();
        }

        @Override
        public void setStatus(final int sc) {

            throw new UnsupportedOperationException();
        }

        @Override
        public void setStatus(final int sc, final String sm) {

            throw new UnsupportedOperationException();
        }

        //
        // Not implemented ServletResponse

        @Override
        public void flushBuffer() throws IOException {

            throw new UnsupportedOperationException();
        }

        @Override
        public int getBufferSize() {

            throw new UnsupportedOperationException();
        }

        @Override
        public String getCharacterEncoding() {

            throw new UnsupportedOperationException();
        }

        @Override
        public String getContentType() {

            throw new UnsupportedOperationException();
        }

        @Override
        public Locale getLocale() {

            throw new UnsupportedOperationException();
        }

        @Override
        public ServletOutputStream getOutputStream() throws IOException {

            throw new UnsupportedOperationException();
        }

        @Override
        public PrintWriter getWriter() throws IOException {

            throw new UnsupportedOperationException();
        }

        @Override
        public boolean isCommitted() {

            throw new UnsupportedOperationException();
        }

        @Override
        public void resetBuffer() {

            throw new UnsupportedOperationException();
        }

        @Override
        public void setBufferSize(final int size) {

            throw new UnsupportedOperationException();
        }

        @Override
        public void setCharacterEncoding(final String charset) {

            throw new UnsupportedOperationException();
        }

        @Override
        public void setContentLength(final int len) {

            throw new UnsupportedOperationException();
        }

        @Override
        public void setContentType(final String type) {

            throw new UnsupportedOperationException();
        }

        @Override
        public void setLocale(final Locale loc) {

            throw new UnsupportedOperationException();
        }
    }
}
