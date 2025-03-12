package com.siukatech.poc.react.backend.module.core.business.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MyPermissionDto extends AbstractDto {
    private String userId;
    List<UserPermissionDto> userPermissionList;
}
