package com.example.algamoney.cors;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import com.example.algamoney.config.property.AlgamoneyProperty;

@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class CorsFilter implements Filter {
	
	@Autowired
	private AlgamoneyProperty algamonetProperty;
	


    @Override
    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain)
            throws IOException, ServletException {
        		
        HttpServletRequest request = (HttpServletRequest) req;
		HttpServletResponse response = (HttpServletResponse) resp;
		
		response.setHeader("Access-Control-Allow-Origin", algamonetProperty.getOrigemPermitida() );
        response.setHeader("Access-Control-Allow-Credentials", "true");
		
		if ("OPTIONS".equals(request.getMethod())) {
			response.setHeader("Access-Control-Allow-Methods", "POST, GET, DELETE, PUT, OPTIONS");
        	response.setHeader("Access-Control-Allow-Headers", "Authorization, Content-Type, Accept");
        	response.setHeader("Access-Control-Max-Age", "36000");
			
			response.setStatus(HttpServletResponse.SC_OK);
		} else {
			chain.doFilter(req, resp);
		}
        
    }
    
}
