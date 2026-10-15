package com.shubham.student_management.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private static final Logger log = LoggerFactory.getLogger(JwtAuthenticationFilter.class);

    private final JwtUtil jwtUtil;
    private final CustomUserDetailsService userDetailsService;

    public JwtAuthenticationFilter(JwtUtil jwtUtil, CustomUserDetailsService userDetailsService) {
        this.jwtUtil = jwtUtil;
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        // 1. Read the header: "Authorization: Bearer eyJhbGci..."
        String header = request.getHeader("Authorization");

        if (header != null && header.startsWith("Bearer ")) {
            String token = header.substring(7);   // remove the "Bearer " prefix

            try {
                // 2. Get the username out of the token (also checks the signature)
                String username = jwtUtil.extractUsername(token);

                // 3. Only continue if nobody is authenticated yet for this request
                if (username != null
                        && SecurityContextHolder.getContext().getAuthentication() == null) {

                    // 4. Load the user from MySQL
                    UserDetails userDetails = userDetailsService.loadUserByUsername(username);

                    // 5. Validate the token against that user
                    if (jwtUtil.validateToken(token, userDetails.getUsername())) {

                        UsernamePasswordAuthenticationToken authentication =
                                new UsernamePasswordAuthenticationToken(
                                        userDetails, null, userDetails.getAuthorities());
                        authentication.setDetails(
                                new WebAuthenticationDetailsSource().buildDetails(request));

                        // 6. This line means "this request is logged in as <username>"
                        SecurityContextHolder.getContext().setAuthentication(authentication);

                        log.info("JWT accepted for user '{}' on {}", username, request.getRequestURI());
                    }
                }
            } catch (Exception e) {
                // Bad, expired or tampered token: stay unauthenticated, don't crash
                log.warn("JWT rejected: {}", e.getMessage());
            }
        }

        // 7. Always pass the request on to the next filter
        filterChain.doFilter(request, response);
    }
}