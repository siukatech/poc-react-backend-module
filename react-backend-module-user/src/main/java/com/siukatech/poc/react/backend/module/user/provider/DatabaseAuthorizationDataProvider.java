package com.siukatech.poc.react.backend.module.user.provider;

import com.siukatech.poc.react.backend.module.core.business.dto.MyKeyDto;
import com.siukatech.poc.react.backend.module.core.business.dto.UserDossierDto;
import com.siukatech.poc.react.backend.module.core.business.dto.UserDto;
import com.siukatech.poc.react.backend.module.core.business.dto.UserPermissionDto;
import com.siukatech.poc.react.backend.module.core.caching.config.DefaultCachingConfig;
import com.siukatech.poc.react.backend.module.core.global.config.AppCoreProp;
import com.siukatech.poc.react.backend.module.core.security.provider.AuthorizationDataProvider;
import com.siukatech.poc.react.backend.module.user.entity.UserEntity;
import com.siukatech.poc.react.backend.module.user.entity.UserPermissionEntity;
import com.siukatech.poc.react.backend.module.user.repository.UserPermissionRepository;
import com.siukatech.poc.react.backend.module.user.repository.UserRepository;
import com.siukatech.poc.react.backend.module.user.repository.UserViewRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import java.util.List;

import static com.siukatech.poc.react.backend.module.core.security.provider.AuthorizationDataCacheKeyGenerator.CACHE_KEY_findPermissionsByUserIdAndTokenValue;
import static com.siukatech.poc.react.backend.module.core.security.provider.AuthorizationDataCacheKeyGenerator.CACHE_KEY_findUserByUserIdAndTokenValue;

@Slf4j
@EntityScan(basePackages = {"com.siukatech.poc.react.backend.module.core.security.provider.database.entity"})  // "**" means all packages
@EnableJpaRepositories("com.siukatech.poc.react.backend.module.core.security.provider.database.repository")    // "**" means all packages
//@Service
public class DatabaseAuthorizationDataProvider implements AuthorizationDataProvider {

    private final AppCoreProp appCoreProp;
    private final ModelMapper modelMapper;
//    private final UserService userService;
    private final UserRepository userRepository;
    private final UserPermissionRepository userPermissionRepository;
    private final UserViewRepository userViewRepository;

    public DatabaseAuthorizationDataProvider(
            AppCoreProp appCoreProp,
            ModelMapper modelMapper,
//            UserService userService
            UserRepository userRepository,
            UserPermissionRepository userPermissionRepository,
            UserViewRepository userViewRepository
    ) {
        log.debug("constructor");
        this.appCoreProp = appCoreProp;
        this.modelMapper = modelMapper;
//        this.userService = userService;
        this.userRepository = userRepository;
        this.userPermissionRepository = userPermissionRepository;
        this.userViewRepository = userViewRepository;
    }

//    @Override
    @Cacheable(value = {DefaultCachingConfig.CACHE_NAME_DEFAULT}
            , key = "'" + CACHE_KEY_findUserByUserIdAndTokenValue + "' + #userId")
    public UserDto findUserByUserIdAndTokenValue(String userId, String tokenValue) {
        log.debug("findByUserIdAndTokenValue - start, userId: [{}]", userId);
//        UserDto userDto = userService.findByUserId(userId);
        UserEntity userEntity = this.userRepository.findByUserId(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found [%s]".formatted(userId)));
        log.debug("findByUserIdAndTokenValue - modelMapper: [" + this.modelMapper + "], userId: [{}]", userId);
        UserDto userDto = this.modelMapper.map(userEntity, UserDto.class);
        log.debug("findByUserIdAndTokenValue - end, userId: [{}]", userId);
        return userDto;
    }

//    @Override
    @Cacheable(value = {DefaultCachingConfig.CACHE_NAME_DEFAULT}
            , key = "'" + CACHE_KEY_findPermissionsByUserIdAndTokenValue + "' + #userId")
    public List<UserPermissionDto> findPermissionsByUserIdAndTokenValue(String userId, String tokenValue) {
        log.debug("findPermissionsByUserIdAndTokenValue - start, userId: [{}]", userId);
//        List<UserPermissionDto> userPermissionDtoList = userService
//                .findPermissionsByUserIdAndApplicationId(userId, appCoreProp.getApplicationId());
        List<UserPermissionEntity> userPermissionEntityList = this.userPermissionRepository
                .findByUserIdAndApplicationId(userId, appCoreProp.getApplicationId());
        List<UserPermissionDto> userPermissionDtoList = userPermissionEntityList.stream()
                .map(userPermissionEntity -> this.modelMapper
                        .map(userPermissionEntity, UserPermissionDto.class))
                .toList();
        log.debug("findPermissionsByUserIdAndTokenValue - end, userId: [{}]", userId);
        return userPermissionDtoList;
    }

    @Override
    @Cacheable(value = {DefaultCachingConfig.CACHE_NAME_AUTH}
//            , key = "'" + CACHE_KEY_findDossierByUserIdAndTokenValue + "' + #userId"
            , keyGenerator = "authorizationDataCacheKeyGenerator"
    )
    public UserDossierDto findDossierByUserIdAndTokenValue(String userId, String tokenValue) {
        log.debug("findDossierByUserIdAndTokenValue - start, userId: [{}]", userId);
        UserEntity userEntity = this.userRepository.findByUserId(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found [%s]".formatted(userId)));
        UserDto userDto = this.modelMapper.map(userEntity, UserDto.class);
        MyKeyDto myKeyDto = this.modelMapper.map(userEntity, MyKeyDto.class);
        List<UserPermissionEntity> userPermissionEntityList = this.userPermissionRepository
                .findByUserIdAndApplicationId(userId, appCoreProp.getApplicationId());
        List<UserPermissionDto> userPermissionDtoList = userPermissionEntityList.stream()
                .map(userPermissionEntity -> this.modelMapper
                        .map(userPermissionEntity, UserPermissionDto.class))
                .toList();
        UserDossierDto userDossierDto = new UserDossierDto(userDto, myKeyDto, userPermissionDtoList);
        log.debug("findDossierByUserIdAndTokenValue - end, userId: [{}]", userId);
        return userDossierDto;
    }

}
