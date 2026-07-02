package com.siukatech.poc.react.backend.module.user.security.constant;

import com.siukatech.poc.react.backend.module.core.security.constant.CoreSecurityConstants;

public interface UserSecurityConstants extends CoreSecurityConstants {

    // 2) Resource Type Constant
    interface ResourceType extends CoreSecurityConstants.ResourceType {
        String MY = "MY";
        String USER = "USER";
    }

}
