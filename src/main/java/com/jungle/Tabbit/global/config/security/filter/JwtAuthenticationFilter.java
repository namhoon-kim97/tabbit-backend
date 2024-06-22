package com.jungle.Tabbit.global.config.security.filter;

import com.jungle.Tabbit.domain.member.service.CustomUserDetailsService;
import com.jungle.Tabbit.global.config.security.SecurityConfig;
import com.jungle.Tabbit.global.config.security.jwt.JwtProvider;
import com.jungle.Tabbit.global.exception.TokenNotFoundException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;

import static com.jungle.Tabbit.global.model.ResponseStatus.*;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtProvider jwtProvider;
    private final CustomUserDetailsService customUserDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String token = getJwtFromRequest(request);

        if (token != null && jwtProvider.validateToken(token)) {
            String username = jwtProvider.getUsernameFromToken(token);

            UserDetails userDetails = customUserDetailsService.loadUserByUsername(username);
            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
            authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

            SecurityContextHolder.getContext().setAuthentication(authentication);
        } else if (token == null && !isAllowedRequest(request)) {
            throw new TokenNotFoundException(FAIL_TOKEN_NOT_FOUND);
        }

        filterChain.doFilter(request, response);
    }

    private String getJwtFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }

    private String normalizeUri(String uri) {
        if (uri.endsWith("/**")) {
            return uri.substring(0, uri.length() - 3);
        }
        return uri;
    }

    private boolean isAllowedRequest(HttpServletRequest request) {
        String uri = request.getRequestURI();
        boolean result = Arrays.stream(SecurityConfig.PERMIT_URI_ARRAY)
                .map(this::normalizeUri)
                .anyMatch(uri::startsWith);

        return result;
    }
}