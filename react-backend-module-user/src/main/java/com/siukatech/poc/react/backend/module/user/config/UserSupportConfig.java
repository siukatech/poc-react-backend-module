package com.siukatech.poc.react.backend.module.user.config;

import com.siukatech.poc.react.backend.module.core.global.config.AppCoreProp;
import com.siukatech.poc.react.backend.module.core.security.provider.AuthorizationDataProvider;
import com.siukatech.poc.react.backend.module.user.provider.DatabaseAuthorizationDataProvider;
import com.siukatech.poc.react.backend.module.user.repository.UserPermissionRepository;
import com.siukatech.poc.react.backend.module.user.repository.UserRepository;
import com.siukatech.poc.react.backend.module.user.repository.UserViewRepository;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Slf4j
@Configuration
//@EntityScan(basePackages = {"com.siukatech.poc.react.backend.core.security.provider.database.entity"})  // "**" means all packages
//@EnableJpaRepositories("com.siukatech.poc.react.backend.core.security.provider.database.repository")    // "**" means all packages
@EntityScan(basePackages = {"com.siukatech.poc.react.backend.core.user.entity"})  // "**" means all packages
@EnableJpaRepositories("com.siukatech.poc.react.backend.core.user.repository")    // "**" means all packages
@ComponentScan(value = {"com.siukatech.poc.react.backend.core.user"})
public class UserSupportConfig {

    @Bean
    @Primary
    public AuthorizationDataProvider databaseAuthorizationDataProvider(
            AppCoreProp appCoreProp
            , ModelMapper modelMapper
//            , UserService userService
            , UserRepository userRepository
            , UserPermissionRepository userPermissionRepository
            , UserViewRepository userViewRepository
    ) {
        log.debug("databaseAuthorizationDataProvider");
        return new DatabaseAuthorizationDataProvider(appCoreProp
                , modelMapper
//                , userService
                , userRepository
                , userPermissionRepository
                , userViewRepository
        );
    }

}
