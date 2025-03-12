package com.siukatech.poc.react.backend.module.core.security.interceptor;

import com.siukatech.poc.react.backend.module.core.security.model.MyAuthenticationToken;
import com.siukatech.poc.react.backend.module.core.util.HttpHeaderUtils;
import com.siukatech.poc.react.backend.module.core.web.micrometer.CorrelationIdHandler;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.io.IOException;

@Slf4j
public class OAuth2ClientHttpRequestInterceptor implements ClientHttpRequestInterceptor {

    public static final String HEADERS_TRACE_PARENT = "tracecore";

    private final CorrelationIdHandler correlationIdHandler;

    public OAuth2ClientHttpRequestInterceptor(CorrelationIdHandler correlationIdHandler) {
        this.correlationIdHandler = correlationIdHandler;
    }

    @Override
    public ClientHttpResponse intercept(HttpRequest request, byte[] body
            , ClientHttpRequestExecution execution) throws IOException {
        // https://stackoverflow.com/a/47046477
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String tokenValue = null;
//        if (authentication instanceof OAuth2AuthenticationToken oAuth2AuthenticationToken) {
////            OAuth2AuthenticationToken oAuth2AuthenticationToken = (OAuth2AuthenticationToken) authentication;
//            tokenValue = oAuth2AuthenticationToken.getPrincipal().getAttribute(KeycloakJwtAuthenticationConverter.ATTR_TOKEN_VALUE);
        //
        // authentication from SecurityContext is null when the process is KeycloakJwtAuthenticationConverter.
        // authentication is still preparing at that moment.
        // So header is required to configured at that moment.
        if (authentication instanceof MyAuthenticationToken myAuthenticationToken) {
            tokenValue = myAuthenticationToken.getTokenValue();
        }
        HttpHeaderUtils.logHttpHeaders(request.getHeaders());
//        log.debug("intercept - request.getHeaders: [{}]", request.getHeaders());
        log.debug("intercept - authentication: [{}], tokenValue: [{}]", authentication, tokenValue);
        log.debug("intercept - correlationIdHandler.getCorrelationId: [{}]", correlationIdHandler.getCorrelationId());
        log.debug("intercept - request.URI: [{}]"
                        + ", authentication.getName: [{}]"
                        + ", authentication.getCredentials: [{}]"
                        + ", authentication.getClass.getName: [{}]"
                        + ", tokenValue: [{}]"
                , request.getURI()
                , (authentication == null ? "NULL" : authentication.getName())
                , (authentication == null ? "NULL" : authentication.getCredentials())
                , (authentication == null ? "NULL" : authentication.getClass().getName())
                , tokenValue
        );
        // Reference:
        // https://stackoverflow.com/a/75526783
        String correlationId = correlationIdHandler.getCorrelationId();
        if (StringUtils.isNotEmpty(correlationId)) {
            request.getHeaders().add(HEADERS_TRACE_PARENT, correlationId);
        }
        if (tokenValue != null) {
            request.getHeaders().set(HttpHeaders.AUTHORIZATION, "Bearer " + tokenValue);
        }
//        return null;
        ClientHttpResponse response = execution.execute(request, body);
        //
//        if (log.isDebugEnabled()) {
//            InputStreamReader inputStreamReader = new InputStreamReader(response.getBody(), StandardCharsets.UTF_8);
//            String resBody = new BufferedReader(inputStreamReader).lines().collect(Collectors.joining("\n"));
//            log.debug("intercept - resBody: [{}]", resBody);
//        }
        //
        return response;
    }

}
