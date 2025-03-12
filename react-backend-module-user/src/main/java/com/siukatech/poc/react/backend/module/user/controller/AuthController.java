package com.siukatech.poc.react.backend.module.user.controller;


import com.siukatech.poc.react.backend.module.user.form.auth.LoginForm;
import com.siukatech.poc.react.backend.module.user.form.auth.RefreshTokenForm;
import com.siukatech.poc.react.backend.module.user.form.auth.TokenRes;
import com.siukatech.poc.react.backend.module.user.service.AuthService;
import com.siukatech.poc.react.backend.module.core.web.annotation.v1.PublicApiV1Controller;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URISyntaxException;

@Slf4j
@PublicApiV1Controller
public class AuthController {

//    @Value("${security.oauth2.client.registration.keycloak.client-id}")
//    private String clientId;
//    @Value("${security.oauth2.client.registration.keycloak.client-secret}")
//    private String clientSceret;

    //    private final OAuth2ClientProperties oAuth2ClientProperties;
//    private final RestTemplate oauth2ClientRestTemplate;
//    private final ObjectMapper objectMapper;
    private final AuthService authService;

    public AuthController(
//            OAuth2ClientProperties oAuth2ClientProperties
//            , RestTemplate oauth2ClientRestTemplate
//            , ObjectMapper objectMapper
//            ,
            AuthService authService) {
//        this.oAuth2ClientProperties = oAuth2ClientProperties;
//        this.oauth2ClientRestTemplate = oauth2ClientRestTemplate;
//        this.objectMapper = objectMapper;
        this.authService = authService;
    }


    @GetMapping(value = "/auth/login/{clientName}")
    public void doAuthCodeLogin(
            HttpServletResponse response, @PathVariable String clientName
            , @RequestParam(required = false) String codeChallenge) {
        String authCodeLoginUrl = this.authService.getAuthCodeLoginUrl(clientName, codeChallenge);
        log.debug("doAuthCodeLogin - clientName: [{}], authCodeLoginUrl: [{}], codeChallenge: [{}]"
                , clientName, authCodeLoginUrl, codeChallenge);
        response.setHeader(HttpHeaders.LOCATION, authCodeLoginUrl);
        response.setStatus(HttpStatus.FOUND.value());
    }

    @PostMapping(value = "/auth/login/{clientName}")
    public ResponseEntity doPasswordLogin(@PathVariable String clientName
            , @RequestBody @Valid LoginForm loginForm) {
        TokenRes tokenRes = this.authService.resolvePasswordTokenRes(clientName, loginForm);
        log.debug("doPasswordLogin - clientName: [" + clientName
                + "], loginForm.getUsername: [" + loginForm.getUsername()
                + "], tokenRes: [" + tokenRes
                + "]");
        return ResponseEntity.ok(tokenRes);
    }

    @PostMapping(value = "/auth/token/{clientName}/{code}")
    public ResponseEntity doAuthCodeToken(
            @PathVariable String clientName, @PathVariable String code
            , @RequestParam(required = false) String codeVerifier
    ) {
        TokenRes tokenRes = this.authService.resolveAuthCodeTokenRes(clientName, code, codeVerifier);
        log.info("doAuthCodeToken - clientName: [" + clientName
                + "], code: [" + code
                + "], tokenRes: [" + tokenRes
                + "], codeVerifier: [" + codeVerifier
                + "]");
        return ResponseEntity.ok(tokenRes);
    }

    @PostMapping(value = "/auth/refresh-token/{clientName}")
    public ResponseEntity doAuthTokenRefresh(@PathVariable String clientName
            , @RequestBody @Valid RefreshTokenForm refreshTokenForm) {
        TokenRes tokenRes = this.authService.resolveRefreshTokenTokenRes(clientName, refreshTokenForm);
        log.debug("doPasswordLogin - clientName: [" + clientName
                + "], refreshTokenForm: [" + refreshTokenForm
                + "], tokenRes: [" + tokenRes
                + "]");
        return ResponseEntity.ok(tokenRes);
    }

    @PostMapping("/logout")
    public ResponseEntity doAuthLogout(HttpServletRequest request) throws URISyntaxException {
        try {
            String requestURL = request.getRequestURL().toString();
            String requestURI = request.getRequestURI();
            String hostName = requestURL.substring(0, requestURL.lastIndexOf(requestURI));
            String logoutApi = hostName + "/logout";
            log.debug("doAuthLogout - logoutApi: [{}]"
                            + ", request.getLocalName: [{}]"
                            + ", request.getLocalPort: [{}]"
                            + ", request.getServerName: [{}]"
                            + ", request.getServerPort: [{}]"
                            + ", request.getPathInfo: [{}]"
                            + ", request.getRequestURI: [{}]"
                            + ", request.getProtocol: [{}]"
                            + ", request.getRequestURL: [{}]"
                    , logoutApi
                    , request.getLocalName()
                    , request.getLocalPort()
                    , request.getServerName()
                    , request.getServerPort()
                    , request.getPathInfo()
                    , request.getRequestURI()
                    , request.getProtocol()
                    , request.getRequestURL().toString()
            );
//            Map map = this.oauth2ClientRestTemplate.postForObject(new URI(logoutApi), null, HashMap.class);
            HttpStatusCode httpStatusCode = this.authService.doAuthLogout(logoutApi);
            log.debug("doAuthLogout - httpStatusCode: [{}]", httpStatusCode);
            return ResponseEntity.status(httpStatusCode.value()).build();
        } catch (Exception e) {
            log.error("doAuthLogout", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

}
