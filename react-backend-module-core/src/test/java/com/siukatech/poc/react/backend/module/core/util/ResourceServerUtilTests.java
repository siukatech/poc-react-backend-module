package com.siukatech.poc.react.backend.module.core.util;

import com.nimbusds.jwt.SignedJWT;
import com.siukatech.poc.react.backend.module.core.AbstractUnitTests;
import com.siukatech.poc.react.backend.module.core.security.resourceserver.OAuth2ResourceServerExtProp;
import org.springframework.boot.autoconfigure.security.oauth2.client.OAuth2ClientProperties;
import org.springframework.boot.autoconfigure.security.oauth2.resource.OAuth2ResourceServerProperties;

import java.text.ParseException;

import static org.assertj.core.api.Assertions.assertThat;

public class ResourceServerUtilTests extends AbstractUnitTests {

    public void test_getSignedJWT_basic() throws ParseException {
        // given
        String token = "";

        // when
        SignedJWT signedJWT = ResourceServerUtil.getSignedJWT(token);

        // then
        assertThat(signedJWT.getJWTClaimsSet().getIssuer()).isEqualTo("");
    }

    public void test_getIssuerUri_basic() throws ParseException {
        // given
        String token = "";
        String issuerParam = "";

        // when
        String issuerRet = ResourceServerUtil.getIssuerUri(token);

        // then
        assertThat(issuerRet).isEqualTo(issuerParam);
    }

    public void test_getClientName_oauth2ClientProperties() {
        // given
        String token = "";
        String issuerParam = "";
        String clientName = null;
        OAuth2ClientProperties oAuth2ClientProperties = null;
        String issuerUri = null;

        // when
        String clientNameRet = ResourceServerUtil.getClientName(oAuth2ClientProperties, issuerUri);

        // then
        assertThat(clientNameRet).isEqualTo(clientName);
    }

    public void test_getClientName_oAuth2ResourceServerExtProp() {
        // given
        String token = "";
        String issuerParam = "";
        String clientName = null;
        OAuth2ResourceServerExtProp oAuth2ResourceServerExtProp = null;
        String issuerUri = null;

        // when
        String clientNameRet = ResourceServerUtil.getClientName(oAuth2ResourceServerExtProp, issuerUri);

        // then
        assertThat(clientNameRet).isEqualTo(clientName);
    }

    public void test_getResourceServerPropJwt_basic() {
        // given
        OAuth2ResourceServerExtProp oAuth2ResourceServerExtProp = null;
        String clientName = null;

        // when
        OAuth2ResourceServerProperties.Jwt jwtRet = ResourceServerUtil
                .getResourceServerPropJwt(oAuth2ResourceServerExtProp, clientName);

        // then
        assertThat(jwtRet).isNotNull();

    }

    public void test_getIssuerUri_OAuth2ResourceServerExtProp() {
        // given
        OAuth2ResourceServerExtProp oAuth2ResourceServerExtProp = null;
        String issuerUriSrc = null;

        // when
        String issuerRet = ResourceServerUtil
                .getIssuerUri(oAuth2ResourceServerExtProp, issuerUriSrc);

        // then
        assertThat(issuerRet).isNotNull();
    }

}
