package com.siukatech.poc.react.backend.module.user.provider;

import com.siukatech.poc.react.backend.module.core.business.dto.UserDossierDto;
import com.siukatech.poc.react.backend.module.core.business.dto.UserDto;
import com.siukatech.poc.react.backend.module.core.business.dto.UserPermissionDto;
import com.siukatech.poc.react.backend.module.core.caching.config.AbstractCachingConfig;
import com.siukatech.poc.react.backend.module.core.global.config.AppCoreProp;
import com.siukatech.poc.react.backend.module.core.security.provider.AuthorizationDataProvider;
import com.siukatech.poc.react.backend.module.user.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import java.util.List;

@Slf4j
@EntityScan(basePackages = {"com.siukatech.poc.react.backend.module.core.security.provider.database.entity"})  // "**" means all packages
@EnableJpaRepositories("com.siukatech.poc.react.backend.module.core.security.provider.database.repository")    // "**" means all packages
//@Service
public class DatabaseAuthorizationDataProvider implements AuthorizationDataProvider {

    private final AppCoreProp appCoreProp;
    private final UserService userService;
//    private final ModelMapper modelMapper;
//    private final UserRepository userRepository;
//    private final UserPermissionRepository userPermissionRepository;
//    private final UserViewRepository userViewRepository;

    public DatabaseAuthorizationDataProvider(
            AppCoreProp appCoreProp
            , UserService userService
//            , ModelMapper modelMapper
//            , UserRepository userRepository
//            , UserPermissionRepository userPermissionRepository
//            , UserViewRepository userViewRepository
    ) {
        log.debug("constructor");
        this.appCoreProp = appCoreProp;
        this.userService = userService;
//        this.modelMapper = modelMapper;
//        this.userRepository = userRepository;
//        this.userPermissionRepository = userPermissionRepository;
//        this.userViewRepository = userViewRepository;
    }

    @Cacheable(value = {AbstractCachingConfig.CACHE_NAME_AUTH}
//            , key = "'" + CACHE_KEY_findUserByUserIdAndTokenValue + "' + #userId"
            , keyGenerator = "authorizationDataUserCacheKeyGenerator"
    )
//    @Override
    public UserDto findUserByUserIdAndTokenValue(String userId, String tokenValue) {
        log.debug("findByUserIdAndTokenValue - start, userId: [{}]", userId);
        UserDto userDto = userService.findUserByUserId(userId);
//        UserEntity userEntity = this.userRepository.findByUserId(userId)
//                .orElseThrow(() -> new EntityNotFoundException("User not found [%s]".formatted(userId)));
//        log.debug("findByUserIdAndTokenValue - modelMapper: [" + this.modelMapper + "], userId: [{}]", userId);
//        UserDto userDto = this.modelMapper.map(userEntity, UserDto.class);
        log.debug("findByUserIdAndTokenValue - end, userId: [{}]", userId);
        return userDto;
    }

    @Cacheable(value = {AbstractCachingConfig.CACHE_NAME_AUTH}
//            , key = "'" + CACHE_KEY_findPermissionsByUserIdAndTokenValue + "' + #userId"
            , keyGenerator = "authorizationDataPermissionCacheKeyGenerator"
    )
//    @Override
    public List<UserPermissionDto> findPermissionsByUserIdAndTokenValue(String userId, String tokenValue) {
        String applicationId = appCoreProp.getApplicationId();
        applicationId = null; // all applications
        log.debug("findPermissionsByUserIdAndTokenValue - start"
                        + ", userId: [{}], applicationId: [{}], appCoreProp.getApplicationId: [{}]"
                , userId, applicationId, appCoreProp.getApplicationId());
        List<UserPermissionDto> userPermissionDtoList = userService
                .findPermissionsByUserIdAndApplicationId(userId, applicationId);
//        List<UserPermissionEntity> userPermissionEntityList = this.userPermissionRepository
//                .findByUserIdAndApplicationId(userId, appCoreProp.getApplicationId());
//        List<UserPermissionDto> userPermissionDtoList = userPermissionEntityList.stream()
//                .map(userPermissionEntity -> this.modelMapper
//                        .map(userPermissionEntity, UserPermissionDto.class))
//                .toList();
        log.debug("findPermissionsByUserIdAndTokenValue - end, userId: [{}]", userId);
        return userPermissionDtoList;
    }

    @Cacheable(value = {AbstractCachingConfig.CACHE_NAME_AUTH}
//            , key = "'" + CACHE_KEY_findDossierByUserIdAndTokenValue + "' + #userId"
            , keyGenerator = "authorizationDataDossierCacheKeyGenerator"
    )
    @Override
    public UserDossierDto findDossierByUserIdAndTokenValue(String userId, String tokenValue) {
        String applicationId = appCoreProp.getApplicationId();
        applicationId = null; // all applications
        log.debug("findDossierByUserIdAndTokenValue - start"
                        + ", userId: [{}], applicationId: [{}], appCoreProp.getApplicationId: [{}]"
                , userId, applicationId, appCoreProp.getApplicationId());
        UserDossierDto userDossierDto = this.userService
                .findDossierByUserIdAndApplicationId(userId, applicationId);
//        UserEntity userEntity = this.userRepository.findByUserId(userId)
//                .orElseThrow(() -> new EntityNotFoundException("User not found [%s]".formatted(userId)));
//        UserDto userDto = this.modelMapper.map(userEntity, UserDto.class);
//        MyKeyDto myKeyDto = this.modelMapper.map(userEntity, MyKeyDto.class);
//        List<UserPermissionEntity> userPermissionEntityList = this.userPermissionRepository
//                .findByUserIdAndApplicationId(userId, appCoreProp.getApplicationId());
//        List<UserPermissionDto> userPermissionDtoList = userPermissionEntityList.stream()
//                .map(userPermissionEntity -> this.modelMapper
//                        .map(userPermissionEntity, UserPermissionDto.class))
//                .toList();
//        UserDossierDto userDossierDto = new UserDossierDto(userDto, myKeyDto, userPermissionDtoList);
        log.debug("findDossierByUserIdAndTokenValue - end, userId: [{}]", userId);
        return userDossierDto;
    }

    @CacheEvict(value = {AbstractCachingConfig.CACHE_NAME_AUTH})
    @Override
    public void evictDossierCache() {
        // do nothing
        log.debug("evictDossierCache - cache is evicted");
    }

}
