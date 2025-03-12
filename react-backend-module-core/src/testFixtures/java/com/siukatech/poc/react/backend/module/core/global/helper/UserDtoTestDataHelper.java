package com.siukatech.poc.react.backend.module.core.global.helper;

import com.siukatech.poc.react.backend.module.core.business.dto.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
public class UserDtoTestDataHelper extends AbstractTestDataHelper {

//    public UserEntity prepareUserEntity_basic(boolean withId) {
//        UserEntity userEntity = new UserEntity();
//        userEntity.setUserId("app-user-01");
//        userEntity.setName("App-User-01");
//////        userEntity.setId(1L);
////        userEntity.setId(UUID.randomUUID().toString());
//        if (withId) {
//            userEntity.setId(UUID.randomUUID().toString());
//        }
//        userEntity.setVersionNo(1L);
//        userEntity.setPublicKey("public-key");
//        userEntity.setPrivateKey("private-key");
//        userEntity.setCreatedBy("admin");
//        userEntity.setLastModifiedBy("admin");
//        userEntity.setCreatedDatetime(LocalDateTime.now());
//        userEntity.setLastModifiedDatetime(LocalDateTime.now());
//        return userEntity;
//    }

    public UserDto prepareUserDto_basic() {
        UserDto userDto = new UserDto();
        userDto.setUserId("app-user-01");
        userDto.setName("App-User-01");
        userDto.setPublicKey("public-key");
//        userDto.setPrivateKey("private-key");
        return userDto;
    }

    public UserViewDto prepareUserViewDto_basic() {
        UserViewDto userViewDto = new UserViewDto();
        userViewDto.setUserId("app-user-01");
        userViewDto.setName("App-User-01");
        userViewDto.setPublicKey("public-key");
//        userDto.setPrivateKey("private-key");
        return userViewDto;
    }

    public MyKeyDto prepareMyKeyDto_basic() {
        MyKeyDto myKeyDto = new MyKeyDto();
        myKeyDto.setUserId("app-user-01");
        myKeyDto.setPublicKey("public-key");
        myKeyDto.setPrivateKey("private-key");
        return myKeyDto;
    }

//    public List<UserPermissionEntity> prepareUserPermissionEntityList_basic(boolean withId) {
//        String[][] userPermissionTempsArr = new String[][]{
//                new String[]{"app-user-01", "1", "role-users-01", "frontend-app", "menu.home", "view"}
//                , new String[]{"app-user-01", "1", "role-users-01", "frontend-app", "menu.items", "*"}
////                , new String[]{"app-user-01", "1", "role-users-01", "frontend-app", "menu.shops", "view"}
//                , new String[]{"app-user-01", "1", "role-users-01", "frontend-app", "menu.merchants", "view"}
//        };
//        List<UserPermissionEntity> userPermissionEntityList = new ArrayList<>();
//        for (String[] userPermissionTemps : userPermissionTempsArr) {
//            UserPermissionEntity userPermissionEntity = new UserPermissionEntity();
//            if (withId) {
//                userPermissionEntity.setId(UUID.randomUUID().toString());
//            }
//            userPermissionEntity.setUserId(userPermissionTemps[0]);
//            userPermissionEntity.setUserId(userPermissionTemps[1]);
//            userPermissionEntity.setUserRoleId(userPermissionTemps[2]);
//            userPermissionEntity.setApplicationId(userPermissionTemps[3]);
//            userPermissionEntity.setAppResourceId(userPermissionTemps[4]);
//            userPermissionEntity.setAccessRight(userPermissionTemps[5]);
//            userPermissionEntityList.add(userPermissionEntity);
//        }
//        return userPermissionEntityList;
//    }
//
//    public List<UserPermissionDto> prepareUserPermissionDtoList_basic() {
//        List<UserPermissionEntity> userPermissionEntityList = this.prepareUserPermissionEntityList_basic(true);
//        List<UserPermissionDto> userPermissionDtoList = new ArrayList<>();
//        userPermissionEntityList.forEach(userPermissionEntity -> {
//            userPermissionDtoList.add(UserPermissionMapper.INSTANCE.convertEntityToDto(userPermissionEntity));
//        });
//        return userPermissionDtoList;
//    }

    public List<UserPermissionDto> prepareUserPermissionDtoList_basic() {
        String[][] userPermissionTempsArr = new String[][]{
                new String[]{"app-user-01", "1", "role-users-01", "frontend-app", "menu.home", "view"}
                , new String[]{"app-user-01", "1", "role-users-01", "frontend-app", "menu.items", "*"}
//                , new String[]{"app-user-01", "1", "role-users-01", "frontend-app", "menu.shops", "view"}
                , new String[]{"app-user-01", "1", "role-users-01", "frontend-app", "menu.merchants", "view"}
        };
        List<UserPermissionDto> userPermissionDtoList = new ArrayList<>();
        for (String[] userPermissionTemps : userPermissionTempsArr) {
            UserPermissionDto userPermissionDto = new UserPermissionDto();
            userPermissionDto.setUserId(userPermissionTemps[0]);
            userPermissionDto.setUserId(userPermissionTemps[1]);
            userPermissionDto.setUserRoleId(userPermissionTemps[2]);
            userPermissionDto.setApplicationId(userPermissionTemps[3]);
            userPermissionDto.setAppResourceId(userPermissionTemps[4]);
            userPermissionDto.setAccessRight(userPermissionTemps[5]);
            userPermissionDtoList.add(userPermissionDto);
        }
        return userPermissionDtoList;
    }

    public MyPermissionDto prepareMyPermissionDto_basic() {
        UserDto userDto = this.prepareUserDto_basic();
        List<UserPermissionDto> userPermissionDtoList = this.prepareUserPermissionDtoList_basic();
        MyPermissionDto myPermissionDto = new MyPermissionDto(userDto.getUserId(), userPermissionDtoList);
        return myPermissionDto;
    }

    public UserDossierDto prepareUserDossierDto_basic() {
        UserDto userDto = this.prepareUserDto_basic();
        MyKeyDto myKeyDto = this.prepareMyKeyDto_basic();
        List<UserPermissionDto> userPermissionDtoList = this.prepareUserPermissionDtoList_basic();
        UserDossierDto userDossierDto = new UserDossierDto(userDto, myKeyDto, userPermissionDtoList);
        return userDossierDto;
    }

}
