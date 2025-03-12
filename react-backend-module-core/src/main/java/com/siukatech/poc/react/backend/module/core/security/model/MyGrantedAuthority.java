package com.siukatech.poc.react.backend.module.core.security.model;

import lombok.Builder;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;

@Data
@Builder
public class MyGrantedAuthority implements GrantedAuthority {

    private String userRoleId;
    private String applicationId;
    private String appResourceId;
    private String accessRight;

    @Override
    public String getAuthority() {
        return userRoleId + ":" + appResourceId + ":" + accessRight;
    }

}
