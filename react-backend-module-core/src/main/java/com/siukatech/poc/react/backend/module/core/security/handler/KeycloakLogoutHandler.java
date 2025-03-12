package com.siukatech.poc.react.backend.module.core.security.handler;


import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;


/**
 * Reference:
 * https://stackoverflow.com/q/76497592
 * https://stackoverflow.com/q/75910767
 */
@Slf4j
@Component
public class KeycloakLogoutHandler implements LogoutHandler {

    //@Autowired @Qualifier("securityRestTemplate")
    private RestTemplate restTemplate;

    // This is not working, will cause a circular-dependencies - start
//    public KeycloakLogoutHandler(@Qualifier("securityRestTemplate") RestTemplate restTemplate) {
//        this.restTemplate = restTemplate;
//    }
    // This is not working - end

    public KeycloakLogoutHandler(RestTemplateBuilder restTemplateBuilder) {
        this.restTemplate = this.keycloakRestTemplate(restTemplateBuilder);
    }

    @Bean(name = "keycloakRestTemplate")
    public RestTemplate keycloakRestTemplate(RestTemplateBuilder restTemplateBuilder) {
        return new RestTemplate();
    }

    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        log.debug("logout - authentication: [" + (authentication == null ? "NULL" : authentication) + "]");
//        logoutFromKeycloak((OidcUser) authentication.getPrincipal());
        if (authentication != null) logoutFromKeycloak((OidcUser) authentication.getPrincipal());
        //
        response.setStatus(HttpStatus.OK.value());
    }

    private void logoutFromKeycloak(OidcUser user) {
        String endSessionEndpoint = user.getIssuer() + "/protocol/openid-connect/logout";
        UriComponentsBuilder builder = UriComponentsBuilder
                .fromUriString(endSessionEndpoint)
                .queryParam("id_token_hint", user.getIdToken().getTokenValue());
        String uriComponentsStr = builder.toUriString();

        log.info("logoutFromKeycloak - endSessionEndpoint: [" + endSessionEndpoint
                + "], uriComponentsStr: [" + uriComponentsStr
                + "]");

        ResponseEntity<String> logoutResponse = restTemplate.getForEntity(
                uriComponentsStr, String.class);
        if (logoutResponse.getStatusCode().is2xxSuccessful()) {
            log.info("logoutFromKeycloak - Successfully logged out from Keycloak");
        } else {
            log.error("logoutFromKeycloak - Could not propagate logout to Keycloak");
        }
    }

}

