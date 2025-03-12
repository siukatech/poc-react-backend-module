package com.siukatech.poc.react.backend.module.user.mapper;

import com.siukatech.poc.react.backend.module.core.business.dto.UserDto;
import com.siukatech.poc.react.backend.module.core.business.dto.UserViewDto;
import com.siukatech.poc.react.backend.module.core.business.mapper.AbstractMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

@Mapper
public interface UserMapper extends AbstractMapper {

    public UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    @Mappings(value = {
            @Mapping(target = "appDatetime", ignore = true)
            , @Mapping(target = "dbDatetime", ignore = true)
            , @Mapping(target = "timeZone", ignore = true)
    })
    UserViewDto convertDtoToView(UserDto userDto);

}
