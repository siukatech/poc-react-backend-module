package com.siukatech.poc.react.backend.module.user.service;

import com.siukatech.poc.react.backend.module.core.business.dto.*;
import com.siukatech.poc.react.backend.module.user.caching.UserCachingConfig;
import com.siukatech.poc.react.backend.module.user.entity.UserEntity;
import com.siukatech.poc.react.backend.module.user.entity.UserPermissionEntity;
import com.siukatech.poc.react.backend.module.user.entity.UserViewEntity;
import com.siukatech.poc.react.backend.module.user.repository.UserPermissionRepository;
import com.siukatech.poc.react.backend.module.user.repository.UserRepository;
import com.siukatech.poc.react.backend.module.user.repository.UserViewRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.TimeZone;

@Slf4j
@Service
public class UserService {

    private final ModelMapper modelMapper;
    private final UserRepository userRepository;
    private final UserPermissionRepository userPermissionRepository;
    private final UserViewRepository userViewRepository;

    public UserService(ModelMapper modelMapper
            , UserRepository userRepository
            , UserPermissionRepository userPermissionRepository, UserViewRepository userViewRepository) {
        this.modelMapper = modelMapper;
        this.userRepository = userRepository;
        this.userPermissionRepository = userPermissionRepository;
        this.userViewRepository = userViewRepository;
    }

//    public UserDto findByUserId(String targetUserId) {
//        UserEntity userEntity = this.userRepository.findByUserId(targetUserId)
//                .orElseThrow(() -> new EntityNotFoundException("User not found [%s]".formatted(targetUserId)));
//        log.debug("findByUserId - modelMapper: [" + this.modelMapper + "]");
//        UserDto userDto = this.modelMapper.map(userEntity, UserDto.class);
//        userDto.setCurrentDatetime(LocalDateTime.now());
//        return userDto;
//    }

    public UserDto findUserByUserId(String targetUserId) {
        UserEntity userEntity = this.userRepository.findByUserId(targetUserId)
                .orElseThrow(() -> new EntityNotFoundException("User not found [%s]".formatted(targetUserId)));
        log.debug("findUserByUserId - modelMapper: [" + this.modelMapper + "]");
        UserDto userDto = this.modelMapper.map(userEntity, UserDto.class);
        return userDto;
    }

    public MyKeyDto findKeyByUserId(String targetUserId) {
        UserEntity userEntity = this.userRepository.findByUserId(targetUserId)
                .orElseThrow(() -> new EntityNotFoundException("User not found [%s]".formatted(targetUserId)));
        log.debug("findKeyByUserId - modelMapper: [" + this.modelMapper + "]");
        MyKeyDto myKeyDto = this.modelMapper.map(userEntity, MyKeyDto.class);
        return myKeyDto;
    }

    @Cacheable(value = {UserCachingConfig.CACHE_NAME_USER}
//            , key = "'" + CACHE_KEY_findPermissionsByUserIdAndApplicationId
//            + "' + #targetUserId + '_' + #applicationId"
            , keyGenerator = "userPermissionCacheKeyGenerator"
    )
    public List<UserPermissionDto> findPermissionsByUserIdAndApplicationId(String targetUserId, String applicationId) {
//        List<UserPermissionDto> userPermissionDtoList = this.userRepository.findUserPermissionByUserId(targetUserId);
        List<UserPermissionEntity> userPermissionEntityList = this.userPermissionRepository.findByUserIdAndApplicationId(targetUserId, applicationId);
        List<UserPermissionDto> userPermissionDtoList = userPermissionEntityList.stream()
                .map(userPermissionEntity -> this.modelMapper
                        .map(userPermissionEntity, UserPermissionDto.class))
                .toList();
        return userPermissionDtoList;
    }

    public UserDossierDto findDossierByUserIdAndApplicationId(String targetUserId, String applicationId) {
        UserDto userDto = findUserByUserId(targetUserId);
        MyKeyDto myKeyDto = findKeyByUserId(targetUserId);
        List<UserPermissionDto> userPermissionDtoList = this.findPermissionsByUserIdAndApplicationId(targetUserId, applicationId);
        UserDossierDto userDossierDto = new UserDossierDto(userDto, myKeyDto, userPermissionDtoList);
        return userDossierDto;
    }

    public UserViewDto findViewByUserId(String targetUserId) {
        UserViewEntity userViewEntity = this.userViewRepository.findByUserId(targetUserId)
                .orElseThrow(() -> new EntityNotFoundException("User not found [%s]".formatted(targetUserId)));
//        log.debug("findViewByUserId - modelMapper: [" + this.modelMapper + "]");
        UserViewDto userViewDto = this.modelMapper.map(userViewEntity, UserViewDto.class);
        userViewDto.setAppDatetime(LocalDateTime.now());
        userViewDto.setTimeZone(TimeZone.getDefault());
        log.debug("findViewByUserId - targetUserId: [{}], userViewDto: [{}]", targetUserId, userViewDto);
        return userViewDto;
    }

}
