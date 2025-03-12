package com.siukatech.poc.react.backend.module.core.security.model;

import com.siukatech.poc.react.backend.module.core.business.dto.UserDossierDto;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Collection;

public class MyAuthenticationToken extends OAuth2AuthenticationToken {

    public static final String ATTR_TOKEN_VALUE = "ATTR_TOKEN_VALUE";
    public static final String ATTR_USER_ID = "ATTR_USER_ID";
    public static final String ATTR_PUBLIC_KEY = "ATTR_PUBLIC_KEY";
    public static final String ATTR_PRIVATE_KEY = "ATTR_PRIVATE_KEY";
    public static final String ATTR_USER_DOSSIER_DTO = "ATTR_USER_DOSSIER_DTO";

    /**
     * Constructs an {@code OAuth2AuthenticationToken} using the provided parameters.
     *
     * @param principal                      the users {@code Principal} registered with the OAuth 2.0 Provider
     * @param authorities                    the authorities granted to the users
     * @param authorizedClientRegistrationId the registration identifier of the
     *                                       {@link OAuth2AuthorizedClient Authorized Client}
     */
    public MyAuthenticationToken(OAuth2User principal
            , Collection<? extends GrantedAuthority> authorities
            , String authorizedClientRegistrationId) {
        super(principal, authorities, authorizedClientRegistrationId);
    }

    public String getTokenValue() {
        return this.getPrincipal().getAttribute(ATTR_TOKEN_VALUE);
    }

    public String getUserId() {
        return this.getPrincipal().getAttribute(ATTR_USER_ID);
    }

    public String getPublicKey() {
        return this.getPrincipal().getAttribute(ATTR_PUBLIC_KEY);
    }

    public String getPrivateKey() {
        return this.getPrincipal().getAttribute(ATTR_PRIVATE_KEY);
    }

    public UserDossierDto getUserDossierDto() {
        return this.getPrincipal().getAttribute(ATTR_USER_DOSSIER_DTO);
    }

}
