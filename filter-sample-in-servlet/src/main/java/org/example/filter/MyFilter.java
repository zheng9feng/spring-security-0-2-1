package org.example.filter;

import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;

// Apply this filter to requests for /hello
@WebFilter("/hello")
public class MyFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        System.out.println("MyFilter initialized.");
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        String requestURI = httpRequest.getRequestURI();
        System.out.println("MyFilter is intercepting request for: " + requestURI);

        // Pre-processing: Add an attribute to the request
        request.setAttribute("filterMessage", "This message was added by MyFilter!");

        // Pass the request along the filter chain
        chain.doFilter(request, response);

        // Post-processing (optional): This code executes after the servlet has processed the request
        System.out.println("MyFilter is performing post-processing for: " + requestURI);
    }

    @Override
    public void destroy() {
        System.out.println("MyFilter destroyed.");
    }
}