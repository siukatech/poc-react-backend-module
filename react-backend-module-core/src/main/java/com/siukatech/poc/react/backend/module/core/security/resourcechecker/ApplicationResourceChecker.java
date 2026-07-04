package com.siukatech.poc.react.backend.module.core.security.resourcechecker;

import com.siukatech.poc.react.backend.module.core.security.annotation.PermissionControl;
import com.siukatech.poc.react.backend.module.core.security.annotation.ResourceCheck;
import com.siukatech.poc.react.backend.module.core.security.constant.CoreSecurityConstants;
import com.siukatech.poc.react.backend.module.core.security.model.MyGrantedAuthority;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


@Slf4j
@Component
@ConditionalOnMissingBean(name = "applicationResourceChecker") // Or specified by type if it's the only one
public class ApplicationResourceChecker implements ResourceChecker {

    @Override
    public String getSupportedType() {
        return CoreSecurityConstants.ResourceType.APPLICATION;
    }

    @Override
    public boolean check(ResourceCheck resourceCheck
            , String resourceId
            , Map<String, String> validatedResources
            , PermissionControl permissionControl
            , Authentication authentication) {
        boolean hasAccess = false;
        hasAccess = true;
        log.debug("check - resourceCheck: [{}]"
                        + ", resourceId: [{}]"
                        + ", validatedResources: [{}]"
                        + ", permissionControl: [{}]"
//                        + ", authentication: [{}]"
                        + ", start"
                , resourceCheck, resourceId, validatedResources, permissionControl
//                , authentication
        );
        List<GrantedAuthority> grantedAuthorityList = new ArrayList<>(authentication.getAuthorities());
        long authorityCount = grantedAuthorityList.stream()
                .filter(grantedAuthority -> grantedAuthority instanceof MyGrantedAuthority)
                .map(MyGrantedAuthority.class::cast)
                .filter(mga -> resourceId.equals(mga.getApplicationId()))
                .count()
                ;
        if (authorityCount > 0) {
            hasAccess = true;
        }
        log.debug("check - resourceCheck: [{}]"
                        + ", resourceId: [{}]"
                        + ", validatedResources: [{}]"
                        + ", authorityCount: [{}]"
                        + ", hasAccess: [{}]"
                        + ", end"
                , resourceCheck, resourceId, validatedResources, authorityCount, hasAccess
//                , authentication
        );
        return hasAccess;
    }
}
