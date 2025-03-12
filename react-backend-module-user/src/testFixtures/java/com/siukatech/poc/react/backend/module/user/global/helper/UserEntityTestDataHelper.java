package com.siukatech.poc.react.backend.module.user.global.helper;

import com.siukatech.poc.react.backend.module.core.business.dto.UserPermissionDto;
import com.siukatech.poc.react.backend.module.core.global.helper.UserDtoTestDataHelper;
import com.siukatech.poc.react.backend.module.user.entity.UserEntity;
import com.siukatech.poc.react.backend.module.user.entity.UserPermissionEntity;
import com.siukatech.poc.react.backend.module.user.mapper.UserPermissionMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Slf4j
@Component
public class UserEntityTestDataHelper extends UserDtoTestDataHelper {

    public UserEntity prepareUserEntity_basic(boolean withId) {
        UserEntity userEntity = new UserEntity();
        userEntity.setUserId("app-user-01");
        userEntity.setName("App-User-01");
////        userEntity.setId(1L);
//        userEntity.setId(UUID.randomUUID().toString());
        if (withId) {
            userEntity.setId(UUID.randomUUID().toString());
        }
        userEntity.setVersionNo(1L);
        userEntity.setPublicKey("public-key");
        userEntity.setPrivateKey("private-key");
        userEntity.setCreatedBy("admin");
        userEntity.setLastModifiedBy("admin");
        userEntity.setCreatedDatetime(LocalDateTime.now());
        userEntity.setLastModifiedDatetime(LocalDateTime.now());
        return userEntity;
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
    public List<UserPermissionEntity> prepareUserPermissionEntityList_basic(boolean withId) {
        List<UserPermissionDto> userPermissionDtoList = this.prepareUserPermissionDtoList_basic();
        List<UserPermissionEntity> userPermissionEntityList = new ArrayList<>();
        userPermissionDtoList.forEach(userPermissionDto -> {
            UserPermissionEntity userPermissionEntity = UserPermissionMapper.INSTANCE.convertDtoToEntity(userPermissionDto);
            if (withId) {
                userPermissionEntity.setId(UUID.randomUUID().toString());
            }
            userPermissionEntityList.add(userPermissionEntity);
        });
        return userPermissionEntityList;
    }

}
