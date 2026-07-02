package com.siukatech.poc.react.backend.module.quartz.security.constant;

import com.siukatech.poc.react.backend.module.core.security.constant.CoreSecurityConstants;

public interface QuartzSecurityConstants extends CoreSecurityConstants {

    // 2) Resource Type Constant
    interface ResourceType extends CoreSecurityConstants.ResourceType {
        String SCHEDULER = "SCHEDULER";
        String TRIGGER = "TRIGGER";
        String JOB = "JOB";
    }

}
