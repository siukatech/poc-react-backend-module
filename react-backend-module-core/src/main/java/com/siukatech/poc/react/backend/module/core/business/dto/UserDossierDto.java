package com.siukatech.poc.react.backend.module.core.business.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
//@Builder  // Cannot be used for jackson.databind
public class UserDossierDto extends AbstractDto {
    private UserDto userDto;
    private MyKeyDto myKeyDto;
    private List<UserPermissionDto> userPermissionList;
}
