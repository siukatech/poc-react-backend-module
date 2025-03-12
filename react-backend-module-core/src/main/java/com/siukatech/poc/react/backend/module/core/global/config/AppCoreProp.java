package com.siukatech.poc.react.backend.module.core.global.config;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Data
@Configuration
@ConfigurationProperties(prefix = "app")
//@ConfigurationProperties
public class AppCoreProp {

//    @Bean(name = "securityRestTemplate")
//    public RestTemplate restTemplate() {
//        return new RestTemplate();
//    }

    private String hostName;
    //    private App app;
    private Api api;
    private String applicationId;

    public String getMyUserInfoUrl() {
        String myUserInfoUrl = null;
        if ( StringUtils.isNotEmpty(this.getHostName())
                && (this.getApi() != null && StringUtils.isNotEmpty(this.getApi().getMyUserInfo())) ) {
            myUserInfoUrl = this.getHostName() + this.getApi().getMyUserInfo();
        }
        log.debug("getMyUserInfoUrl - getHostName: [{}], getMyUserInfo: [{}], myUserInfoUrl: [{}]"
                , this.getHostName()
                , (this.getApi() == null ? "NULL" : this.getApi().getMyUserInfo())
                , myUserInfoUrl
        );
        return myUserInfoUrl;
//        return null;
    }

    public String getMyKeyInfoUrl() {
        String myKeyInfoUrl = null;
        if ( StringUtils.isNotEmpty(this.getHostName())
                && (this.getApi() != null && StringUtils.isNotEmpty(this.getApi().getMyKeyInfo())) ) {
            myKeyInfoUrl = this.getHostName() + this.getApi().getMyKeyInfo();
        }
        log.debug("getMyKeyInfoUrl - getHostName: [{}], getMyUserInfo: [{}], myKeyInfoUrl: [{}]"
                , this.getHostName()
                , (this.getApi() == null ? "NULL" : this.getApi().getMyKeyInfo())
                , myKeyInfoUrl
        );
        return myKeyInfoUrl;
    }

    public String getMyPermissionInfoUrl() {
        String myPermissionInfoUrl = null;
        if ( StringUtils.isNotEmpty(this.getHostName())
                && (this.getApi() != null && StringUtils.isNotEmpty(this.getApi().getMyPermissionInfo())) ) {
            myPermissionInfoUrl = this.getHostName() + this.getApi().getMyPermissionInfo();
        }
        log.debug("getMyPermissionInfoUrl - getHostName: [{}], getMyPermissionInfo: [{}], myPermissionInfoUrl: [{}]"
                , this.getHostName()
                , (this.getApi() == null ? "NULL" : this.getApi().getMyPermissionInfo())
                , myPermissionInfoUrl
        );
        return myPermissionInfoUrl;
    }

    public String getMyUserDossierUrl() {
        String myUserDossierUrl = null;
        if ( StringUtils.isNotEmpty(this.getHostName())
                && (this.getApi() != null && StringUtils.isNotEmpty(this.getApi().getMyUserDossier())) ) {
            myUserDossierUrl = this.getHostName() + this.getApi().getMyUserDossier();
        }
        log.debug("getMyUserDossierUrl - getHostName: [{}], getMyUserDossier: [{}], myUserDossierUrl: [{}]"
                , this.getHostName()
                , (this.getApi() == null ? "NULL" : this.getApi().getMyUserDossier())
                , myUserDossierUrl
        );
        return myUserDossierUrl;
    }

//    @Data
//    public static class App {
//        private String hostName;
//        private Api api;
//    }

    @Data
    public static class Api {
        private String myUserInfo;
        private String myKeyInfo;
        private String myPermissionInfo;
        private String myUserDossier;
    }
}
