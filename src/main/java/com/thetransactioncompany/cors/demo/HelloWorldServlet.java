package com.thetransactioncompany.cors.demo;


import java.io.*;

import javax.servlet.*;
import javax.servlet.http.*;


/**
 * Simple servlet for testing CORS requests.
 *
 * @author Vladimir Dzhuvinov
 */
public class HelloWorldServlet extends HttpServlet {


	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public void service(HttpServletRequest request, HttpServletResponse response)
		throws IOException, ServletException {
	
		response.setContentType("text/plain");
		
		response.addHeader("X-Test-1", "Hello world!");
		response.addHeader("X-Test-2", "1, 2, 3");
			
		PrintWriter out = response.getWriter();
		
		out.println("[HTTP " + request.getMethod() + "] Hello world!");
		
		out.println("");
		
		out.println("CORS Filter request tags: ");
		out.println("\tcors.isCorsRequest: " + request.getAttribute("cors.isCorsRequest"));
		out.println("\tcors.origin: " + request.getAttribute("cors.origin"));
		out.println("\tcors.requestType: " + request.getAttribute("cors.requestType"));
		out.println("\tcors.requestHeaders: " + request.getAttribute("cors.requestHeaders"));
	}
}
