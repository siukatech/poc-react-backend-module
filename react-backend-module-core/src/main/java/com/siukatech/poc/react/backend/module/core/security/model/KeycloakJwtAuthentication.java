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

    private final String userNameAttribute;

    public KeycloakJwtAuthentication(Jwt jwt
            , Collection<? extends GrantedAuthority> authorities
            , String userNameAttribute) {
        super(jwt, authorities);
        this.userNameAttribute = userNameAttribute;
    }
    //Note that this time getName() is overridden instead of getPrincipal()
    @Override
    public String getName() {
        // Add userNameAttribute to constructor and use this as key to get from token
        String name = getToken().getClaimAsString(this.userNameAttribute);
        return name;
    }

}

