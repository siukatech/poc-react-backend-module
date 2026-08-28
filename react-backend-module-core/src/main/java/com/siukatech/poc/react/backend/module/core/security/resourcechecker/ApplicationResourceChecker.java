package com.siukatech.poc.react.backend.module.core.security.resourcechecker;

import com.siukatech.poc.react.backend.module.core.security.annotation.PermissionControl;
import com.siukatech.poc.react.backend.module.core.security.annotation.ResourceCheck;
import com.siukatech.poc.react.backend.module.core.security.aop.ReqVariableData;
import com.siukatech.poc.react.backend.module.core.security.constant.CoreSecurityConstants;
import com.siukatech.poc.react.backend.module.core.security.model.MyGrantedAuthority;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.*;


@Slf4j
@Component
//@ConditionalOnMissingBean(name = "applicationResourceChecker") // Or specified by type if it's the only one
public class ApplicationResourceChecker implements ResourceChecker {

    public static final String APPLICATION_ID = "applicationId";

    @Override
    public String getSupportedType() {
        return CoreSecurityConstants.ResourceType.APPLICATION;
    }

    @Override
    public ResourceCheckResult check(ResourceCheck resourceCheck
//            , String resourceId
//            , Map<String, String> validatedResources
            , ReqVariableData reqVariableData
            , Map<String, ResourceCheckResult> validatedResources
            , PermissionControl permissionControl
            , Authentication authentication
    ) {
        boolean hasAccess = false;
        hasAccess = true;
        log.debug("check - resourceCheck: [{}]"
//                        + ", resourceId: [{}]"
                        + ", reqVariableData: [{}]"
                        + ", validatedResources: [{}]"
                        + ", permissionControl: [{}]"
//                        + ", authentication: [{}]"
                        + ", start"
                , resourceCheck
//                , resourceId
                , reqVariableData
                , validatedResources
                , permissionControl
//                , authentication
        );
        String[] applicationIds = reqVariableData.getParamVarMap().get(APPLICATION_ID);
        String resourceId = (Objects.nonNull(applicationIds) && applicationIds.length > 0)?applicationIds[0]:null;
        List<GrantedAuthority> grantedAuthorityList = new ArrayList<>(authentication.getAuthorities());
        long authorityCount = grantedAuthorityList.stream()
                .filter(grantedAuthority -> grantedAuthority instanceof MyGrantedAuthority)
                .map(MyGrantedAuthority.class::cast)
                .filter(mga -> (mga.getApplicationId().equals(resourceId) || Objects.isNull(resourceId)))
                .count();
        if (authorityCount > 0) {
            hasAccess = true;
        }
        Map<String, String> resourceIdMap = new HashMap<>();
        // put the resourceId into resourceIdMap
        resourceIdMap.put(APPLICATION_ID, resourceId);
        ResourceCheckResult resourceCheckResult = new ResourceCheckResult(hasAccess, resourceIdMap);
        log.debug("check - resourceCheck: [{}]"
//                        + ", resourceId: [{}]"
                        + ", reqVariableData: [{}]"
                        + ", validatedResources: [{}]"
                        + ", authorityCount: [{}]"
//                        + ", authentication: [{}]"
                        + ", hasAccess: [{}]"
                        + ", end"
                , resourceCheck
//                , resourceId
                , reqVariableData
                , validatedResources
                , authorityCount
//                , authentication
                , hasAccess
        );
        return resourceCheckResult;
    }
}
