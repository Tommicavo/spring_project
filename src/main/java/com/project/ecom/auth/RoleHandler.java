package com.project.ecom.auth;

import java.io.IOException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.access.AccessDeniedHandler;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.ecom.models.dtos.ErrorDto;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class RoleHandler implements AccessDeniedHandler{

	private final ObjectMapper objectMapper;

    public RoleHandler(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException {
        
		String userRoles = SecurityContextHolder.getContext().getAuthentication().getAuthorities().toString();

		ErrorDto errorDto = new ErrorDto("tokenError", "INSUFFICIENT ROLE. You are: " + userRoles);
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        response.setContentType("application/json");
        response.getWriter().write(objectMapper.writeValueAsString(errorDto));
        response.getWriter().flush();
    }
}
