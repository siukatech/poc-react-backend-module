package com.siukatech.poc.react.backend.module.user.provider;

import com.siukatech.poc.react.backend.module.core.AbstractUnitTests;
import com.siukatech.poc.react.backend.module.core.business.dto.UserDossierDto;
import com.siukatech.poc.react.backend.module.core.business.dto.UserDto;
import com.siukatech.poc.react.backend.module.core.business.dto.UserPermissionDto;
import com.siukatech.poc.react.backend.module.core.global.config.AppCoreProp;
import com.siukatech.poc.react.backend.module.user.entity.UserEntity;
import com.siukatech.poc.react.backend.module.user.entity.UserPermissionEntity;
import com.siukatech.poc.react.backend.module.core.global.helper.UserDtoTestDataHelper;
import com.siukatech.poc.react.backend.module.user.global.helper.UserEntityTestDataHelper;
import com.siukatech.poc.react.backend.module.user.repository.UserPermissionRepository;
import com.siukatech.poc.react.backend.module.user.repository.UserRepository;
import com.siukatech.poc.react.backend.module.user.repository.UserViewRepository;
import com.siukatech.poc.react.backend.module.user.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@Slf4j
@ExtendWith(MockitoExtension.class)
public class DatabaseAuthorizationDataProviderTests extends AbstractUnitTests {

    @InjectMocks
    private DatabaseAuthorizationDataProvider databaseAuthorizationDataProvider;
    @Mock
    private AppCoreProp appCoreProp;
    @Mock
    private UserService userService;
//    @Spy
//    private ModelMapper modelMapper;
//    @Mock
//    private UserRepository userRepository;
//    @Mock
//    private UserPermissionRepository userPermissionRepository;
//    @Mock
//    private UserViewRepository userViewRepository;

    @Spy
    private UserEntityTestDataHelper userEntityTestDataHelper;


    @Test
    void contextLoads() {
        // TODO
        Assertions.assertNotNull(log);
        log.debug("contextLoads - testing logging");
    }

    @Test
    void test_findUserByUserIdAndTokenValue_basic() {
        // given
//        UserEntity userEntity = this.userEntityTestDataHelper.prepareUserEntity_basic(false);
//        String targetUserId = userEntity.getUserId();
        UserDto userDtoMock = this.userEntityTestDataHelper.prepareUserDto_basic();
        String targetUserId = userDtoMock.getUserId();
        String tokenValue = "tokenValue";
        //
//        when(this.userRepository.findByUserId(anyString())).thenReturn(Optional.of(userEntity));
        when(this.userService.findUserByUserId(anyString())).thenReturn(userDtoMock);

        // when
        UserDto userDto = this.databaseAuthorizationDataProvider
                .findUserByUserIdAndTokenValue(targetUserId, tokenValue);

        // then
//        assertThat(userDto.getUserId()).isEqualTo(userEntity.getUserId());
        assertThat(userDto.getUserId()).isEqualTo(userDtoMock.getUserId());
    }

    @Test
    void test_findPermissionsByUserIdAndTokenValue_basic() {
        // given
//        UserEntity userEntity = this.userEntityTestDataHelper.prepareUserEntity_basic(false);
//        List<UserPermissionEntity> userPermissionEntityList = this.userEntityTestDataHelper
//                .prepareUserPermissionEntityList_basic(false);
//        String targetUserId = userEntity.getUserId();
        UserDto userDtoMock = this.userEntityTestDataHelper.prepareUserDto_basic();
        List<UserPermissionDto> userPermissionDtoListMock = this.userEntityTestDataHelper
                .prepareUserPermissionDtoList_basic();
        String targetUserId = userDtoMock.getUserId();
        String tokenValue = "tokenValue";
        String applicationId = "applicationId";
        //
        when(this.appCoreProp.getApplicationId()).thenReturn(applicationId);
//        when(this.userPermissionRepository.findByUserIdAndApplicationId(anyString()
////                , isNull()
//                , anyString()
//        )).thenReturn(userPermissionEntityList);
        when(this.userService.findPermissionsByUserIdAndApplicationId(anyString(), nullable(String.class)))
                .thenReturn(userPermissionDtoListMock);

        // when
        List<UserPermissionDto> userPermissionDtoList = this.databaseAuthorizationDataProvider
                .findPermissionsByUserIdAndTokenValue(targetUserId, tokenValue);

        // then
//        assertThat(userPermissionDtoList.size()).isEqualTo(userPermissionEntityList.size());
        assertThat(userPermissionDtoList.size()).isEqualTo(userPermissionDtoListMock.size());
    }

    @Test
    void test_findDossierByUserIdAndTokenValue_basic() {
        // given
//        UserEntity userEntity = this.userEntityTestDataHelper.prepareUserEntity_basic(false);
//        List<UserPermissionEntity> userPermissionEntityList = this.userEntityTestDataHelper
//                .prepareUserPermissionEntityList_basic(false);
//        String targetUserId = userEntity.getUserId();
        UserDossierDto userDossierDtoMock = this.userEntityTestDataHelper
                .prepareUserDossierDto_basic();
        String targetUserId = userDossierDtoMock.getUserDto().getUserId();
        String tokenValue = "tokenValue";
        String applicationId = "applicationId";
        //
        when(this.appCoreProp.getApplicationId()).thenReturn(applicationId);
//        when(this.userRepository.findByUserId(anyString())).thenReturn(Optional.of(userEntity));
//        when(this.userPermissionRepository.findByUserIdAndApplicationId(anyString()
////                , isNull()
//                , anyString()
//        )).thenReturn(userPermissionEntityList);
        when(this.userService.findDossierByUserIdAndApplicationId(anyString(), nullable(String.class)))
                .thenReturn(userDossierDtoMock);

        // when
        UserDossierDto userDossierDto = this.databaseAuthorizationDataProvider
                .findDossierByUserIdAndTokenValue(targetUserId, tokenValue);

        // then
//        assertThat(userDossierDto.getUserPermissionList().size())
//                .isEqualTo(userPermissionEntityList.size());
        assertThat(userDossierDto.getUserPermissionList().size())
                .isEqualTo(userDossierDtoMock.getUserPermissionList().size());

    }

}
