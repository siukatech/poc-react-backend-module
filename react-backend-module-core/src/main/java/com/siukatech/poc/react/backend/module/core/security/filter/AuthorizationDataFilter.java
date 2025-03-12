package com.siukatech.poc.react.backend.module.core.security.filter;

import com.siukatech.poc.react.backend.module.core.security.provider.AuthorizationDataProvider;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
@Component
public class AuthorizationDataFilter extends OncePerRequestFilter {

    private final AuthorizationDataProvider authorizationDataProvider;

    public AuthorizationDataFilter(AuthorizationDataProvider authorizationDataProvider) {
        this.authorizationDataProvider = authorizationDataProvider;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response
            , FilterChain filterChain) throws ServletException, IOException {
        log.debug("doFilterInternal - start");

        filterChain.doFilter(request, response);

        log.debug("doFilterInternal - end");
    }

}
