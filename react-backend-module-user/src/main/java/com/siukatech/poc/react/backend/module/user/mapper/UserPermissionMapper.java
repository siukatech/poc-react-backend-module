package com.siukatech.poc.react.backend.module.user.mapper;

import com.siukatech.poc.react.backend.module.core.business.dto.UserPermissionDto;
import com.siukatech.poc.react.backend.module.core.business.mapper.AbstractMapper;
import com.siukatech.poc.react.backend.module.user.entity.UserPermissionEntity;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface UserPermissionMapper extends AbstractMapper {

    UserPermissionMapper INSTANCE = Mappers.getMapper(UserPermissionMapper.class);

    UserPermissionDto convertEntityToDto(UserPermissionEntity userPermissionEntity);
    UserPermissionEntity convertDtoToEntity(UserPermissionDto userPermissionDto);

}
