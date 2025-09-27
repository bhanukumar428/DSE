package com.example.demo.security;


import io.jsonwebtoken.JwtException;
import jakarta.servlet.ServletException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUtil jwtUtil;

	@Override
	protected void doFilterInternal(jakarta.servlet.http.HttpServletRequest request,
			jakarta.servlet.http.HttpServletResponse response, jakarta.servlet.FilterChain filterChain)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		  String authorizationHeader = request.getHeader("Authorization");

	        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
	            String token = authorizationHeader.substring(7);
	            try {
	                if (jwtUtil.validateToken(token)) {
	                    // Token valid; proceed with the request
	                    String email = jwtUtil.extractEmail(token);
	                    System.out.println("Authenticated email: " + email);
	                }
	            } catch (JwtException e) {
	                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid JWT token");
	                return;
	            }
	        }
	        filterChain.doFilter(request, response);
	}
}