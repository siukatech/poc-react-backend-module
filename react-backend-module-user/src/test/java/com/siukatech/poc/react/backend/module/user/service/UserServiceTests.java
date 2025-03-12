package com.siukatech.poc.react.backend.module.user.service;

import com.siukatech.poc.react.backend.module.core.AbstractUnitTests;
import com.siukatech.poc.react.backend.module.core.business.dto.UserDto;
import com.siukatech.poc.react.backend.module.core.business.dto.UserPermissionDto;
import com.siukatech.poc.react.backend.module.core.business.dto.UserViewDto;
import com.siukatech.poc.react.backend.module.user.entity.UserEntity;
import com.siukatech.poc.react.backend.module.user.entity.UserPermissionEntity;
import com.siukatech.poc.react.backend.module.user.entity.UserViewEntity;
import com.siukatech.poc.react.backend.module.user.repository.UserPermissionRepository;
import com.siukatech.poc.react.backend.module.user.repository.UserRepository;
import com.siukatech.poc.react.backend.module.user.repository.UserViewRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@Slf4j
@ExtendWith(value = {MockitoExtension.class
//    , SpringExtension.class
})
//@Extensions(value = {
//    @ExtendWith(value = MockitoExtension.class)
//    , @ExtendWith(value = SpringExtension.class)
//})
// The following @ContextConfiguration and @Import are not working with MockitoExtension.
// Use @Import to add Config class, @ContextConfiguration is not working
////@ContextConfiguration(classes = {WebConfig.class})
//@Import(value = {UserServiceTests.TestConfig.class})
public class UserServiceTests extends AbstractUnitTests {

    @InjectMocks
    private UserService userService;
    @Spy
//    @Autowired
    private ModelMapper modelMapper;
    @Mock
    private UserRepository userRepository;
    @Mock
    private UserPermissionRepository userPermissionRepository;
    @Mock
    private UserViewRepository userViewRepository;

//    @TestConfiguration
//    public static class TestConfig {
//        private MapperConfig mapperConfig = new MapperConfig();
//        @Bean
//        public ModelMapper modelMapper() {
//            log.info("TestConfig.modelMapper - start");
//            return mapperConfig.modelMapper();
//        }
//    }

    @BeforeAll
    public static void init() {
//        final ch.qos.logback.classic.Logger rootLogger = (ch.qos.logback.classic.Logger) LoggerFactory.getLogger(ch.qos.logback.classic.Logger.ROOT_LOGGER_NAME);
//        rootLogger.setLevel(Level.ALL);

        // If sub-class has her own init, then super-class's init is required to trigger manually
        AbstractUnitTests.init();
    }

    @AfterAll()
    public static void terminate() {
        AbstractUnitTests.terminate();
    }

    @BeforeEach
    public void setup() {
//        // get Logback Logger
//        Logger log = (Logger) LoggerFactory.getLogger(this.getClass());
//
//        // create and start a ListAppender
//        ListAppender<ILoggingEvent> listAppender = new ListAppender<>();
//        listAppender.start();
//
//        // add the appender to the log
//        log.addAppender(listAppender);
//        final Logger log = (Logger) LoggerFactory.getLogger(Logger.ROOT_LOGGER_NAME);
//        log.setLevel(Level.ALL);

//        if (modelMapper == null) {
//            MapperConfig mapperConfig = new MapperConfig();
//            modelMapper = mapperConfig.modelMapper();
//        }
        //
        // Need to configure the AmbiguityIgnored and MatchingStrategy again
        if (modelMapper != null) {
//            modelMapper.getConfiguration().setAmbiguityIgnored(true);
            modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        }
        log.debug("setup");
    }

    @AfterEach()
    public void teardown() {
        log.debug("teardown");
    }

    private List<UserPermissionEntity> prepareUserPermissions_basic() {
        String[][] userPermissionTempsArr = new String[][]{
                new String[]{"app-user-01", "1", "role-user-01", "frontend-app", "menu.home", "view"}
                , new String[]{"app-user-01", "1", "role-user-01", "frontend-app", "menu.items", "*"}
//                , new String[]{"app-user-01", "1", "role-user-01", "frontend-app", "menu.shops", "view"}
                , new String[]{"app-user-01", "1", "role-user-01", "frontend-app", "menu.merchants", "view"}
        };
        List<UserPermissionEntity> userPermissionEntityList = new ArrayList<>();
        for (String[] userPermissionTemps : userPermissionTempsArr) {
            UserPermissionEntity userPermissionEntity = new UserPermissionEntity();
            userPermissionEntity.setUserId(userPermissionTemps[0]);
////            userPermissionEntity.setUserId(Long.valueOf(userPermissionTemps[1]));
//            userPermissionEntity.setUserId(userPermissionTemps[1]);
            userPermissionEntity.setUserRoleId(userPermissionTemps[2]);
            userPermissionEntity.setApplicationId(userPermissionTemps[3]);
            userPermissionEntity.setAppResourceId(userPermissionTemps[4]);
            userPermissionEntity.setAccessRight(userPermissionTemps[5]);
            userPermissionEntityList.add(userPermissionEntity);
        }
        return userPermissionEntityList;
    }

    private UserEntity prepareUserEntity_basic() {
        UserEntity userEntity = new UserEntity();
        userEntity.setUserId("app-user-01");
        userEntity.setName("App-User-01");
        userEntity.setPublicKey("public-key");
        userEntity.setPrivateKey("private-key");
        userEntity.setVersionNo(1L);
        return userEntity;
    }

    private UserViewEntity prepareUserViewEntity_basic() {
        UserViewEntity userViewEntity = new UserViewEntity();
        userViewEntity.setUserId("app-user-01");
        userViewEntity.setName("App-User-01");
        userViewEntity.setPublicKey("public-key");
        userViewEntity.setPrivateKey("private-key");
        userViewEntity.setVersionNo(1L);
        return userViewEntity;
    }

    @Test
    public void test_findUserByUserId_basic() {
        // given
        UserEntity userEntity = this.prepareUserEntity_basic();
        when(this.userRepository.findByUserId(anyString())).thenReturn(Optional.of(userEntity));

        // when
        UserDto userDtoActual = this.userService.findUserByUserId("app-user-01");

        // then / verify
        assertThat(userDtoActual.getUserId()).isEqualTo("app-user-01");
    }

    @Test
    public void test_findPermissionsByUserIdAndApplicationId_basic() {
        // given
        List<UserPermissionEntity> userPermissionEntityListTemp = prepareUserPermissions_basic();
        when(this.userPermissionRepository.findByUserIdAndApplicationId(anyString(), anyString())).thenReturn(userPermissionEntityListTemp);
//        doReturn(userPermissionEntityListTemp).when(userPermissionRepository).findByUserIdAndApplicationId(anyString(), anyString());
        log.info("test_findPermissionsByUserIdAndApplicationId_basic - modelMapper.getConfiguration: [{}]"
                , modelMapper.getConfiguration());

        // when
        List<UserPermissionDto> userPermissionDtoListActual = this.userService.findPermissionsByUserIdAndApplicationId("app-user-01", "frontend-app");

        // then / verify
        assertThat(userPermissionDtoListActual.get(0).getUserId()).isEqualTo("app-user-01");
        assertThat(userPermissionDtoListActual.get(0).getApplicationId()).isEqualTo("frontend-app");
    }

    @Test
    public void test_findViewByUserId_basic() {
        // given
        UserViewEntity userViewEntity = this.prepareUserViewEntity_basic();
        when(this.userViewRepository.findByUserId(anyString())).thenReturn(Optional.of(userViewEntity));

        // when
        UserViewDto userViewDtoActual = this.userService.findViewByUserId("app-user-01");

        // then / verify
        assertThat(userViewDtoActual.getUserId()).isEqualTo("app-user-01");
    }

}
