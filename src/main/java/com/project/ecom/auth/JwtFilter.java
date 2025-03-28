package com.project.ecom.auth;

import java.io.IOException;
import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.ecom.models.dtos.ErrorDto;
import com.project.ecom.models.entities.RoleEntity;
import com.project.ecom.models.entities.UserEntity;
import com.project.ecom.services.UserService;
import com.project.ecom.utils.AppLogger;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtFilter extends OncePerRequestFilter {

    @Autowired
    private AppLogger logger;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private ObjectMapper objMapper;

    @Autowired
    private UserService userService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String token = jwtService.getTokenFromRequest(request);
        String tokenError = null;

        if (token == null || token.isEmpty()) {
            tokenError = "NULL OR EMPTY TOKEN";
            logger.log.error(tokenError);
            sendUnauthorizedResponse(response, tokenError);
            return;
        }

        Map<Boolean, String> validationResult = jwtService.validateToken(token);
        boolean isValidToken = validationResult.containsKey(true);
        if (!isValidToken) {
            tokenError = validationResult.get(false);
            sendUnauthorizedResponse(response, tokenError);
            return;
        }

        Long idUser;
        try {
            idUser = Long.parseLong(jwtService.getIdFromToken(token));
        } catch (Exception e) {
            tokenError = "FAILED TO EXTRACT USER ID FROM TOKEN";
            sendUnauthorizedResponse(response, tokenError);
            return;
        }

        UserEntity loggedUser = userService.getUser(idUser);
        if (loggedUser == null) {
            tokenError = "USER NOT FOUND";
            sendUnauthorizedResponse(response, tokenError);
            return;
        }

        Set<RoleEntity> roles = loggedUser.getRoles();
        Collection<GrantedAuthority> authorities = roles.stream()
            .map(role -> new SimpleGrantedAuthority("ROLE_" + role.getNameRole()))
            .collect(Collectors.toList());

        Authentication authentication = new UsernamePasswordAuthenticationToken(loggedUser, null, authorities);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        filterChain.doFilter(request, response);
    }

    private void sendUnauthorizedResponse(HttpServletResponse response, String errorMessage) throws IOException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json");
        response.getWriter().write(objMapper.writeValueAsString(new ErrorDto("tokenError", errorMessage)));
        response.getWriter().flush();
    }

    /*
    Not Authenticated Endpoints:
    Otherwise use one from:
    - Authentication required, no Role Required ->
    @PreAuthorize("isAuthenticated()")

    - Authentication Required, List of Roles Allowed ->
    @PreAuthorize("hasAnyRole('ADMIN', 'DEFAULT')")

    - Authentication Required, Specific Role Required ->
    @PreAuthorize("hasRole('ADMIN')")

    */
    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        String path = request.getServletPath();
        AntPathMatcher pathMatcher = new AntPathMatcher();
        return
            pathMatcher.match("/auth/signin", path) ||
            pathMatcher.match("/auth/login", path);
    }
}
