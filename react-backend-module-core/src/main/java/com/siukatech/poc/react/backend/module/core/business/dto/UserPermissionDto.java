package com.siukatech.poc.react.backend.module.core.business.dto;

import lombok.Data;

@Data
//@EqualsAndHashCode(callSuper = true)
public class UserPermissionDto extends AbstractDto {
    private String userId;
    private String userRoleId;
    private String applicationId;
    private String appResourceId;
    private String accessRight;

}
