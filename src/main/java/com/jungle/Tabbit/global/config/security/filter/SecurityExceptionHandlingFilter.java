package com.jungle.Tabbit.global.config.security.filter;

import com.jungle.Tabbit.global.exception.BusinessLogicException;
import com.jungle.Tabbit.global.model.ResponseStatus;
import com.jungle.Tabbit.global.model.SecurityErrorResponse;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class SecurityExceptionHandlingFilter extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            filterChain.doFilter(request, response);
        } catch (BusinessLogicException ex) {
            setErrorResponse(response, ex.getResponseStatus());
        }
    }

    public void setErrorResponse(HttpServletResponse response, ResponseStatus responseStatus) throws IOException {

        response.setStatus(responseStatus.getStatusCode().value());
        response.setContentType("application/json; charset=UTF-8");

        response.getWriter()
                .write(
                        SecurityErrorResponse
                                .of(responseStatus)
                                .convertToJson()
                );
    }
}
