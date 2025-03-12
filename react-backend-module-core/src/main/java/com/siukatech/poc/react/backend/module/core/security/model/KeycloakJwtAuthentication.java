package com.siukatech.poc.react.backend.module.core.security.model;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.oidc.StandardClaimNames;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;

import java.util.Collection;

/**
 * This is no usage ~~~
 */
@Deprecated
public class KeycloakJwtAuthentication extends JwtAuthenticationToken {

    public KeycloakJwtAuthentication(Jwt jwt, Collection<? extends GrantedAuthority> authorities) {
        super(jwt, authorities);
    }
    //Note that this time getName() is overriden instead of getPrincipal()
    @Override
    public String getName() {
        return getToken().getClaimAsString(StandardClaimNames.PREFERRED_USERNAME);
    }

}

