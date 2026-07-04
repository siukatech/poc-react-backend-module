package com.siukatech.poc.react.backend.module.core.data.config;

import com.siukatech.poc.react.backend.module.core.EnableReactBackend;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Objects;
import java.util.Optional;

@Slf4j
@Configuration
////@AutoConfigureAfter(JpaRepositoriesAutoConfiguration.class)
////@EnableTransactionManagement(proxyTargetClass = true)
@EnableJpaAuditing(auditorAwareRef = "auditorAware")
//@ComponentScan(basePackages = { "com.siukatech.poc.react.backend.module.core.data" })
////@EntityScan(basePackages = {"com.siukatech.poc.react.backend.module.core.data.entity"})
////@EnableJpaRepositories("com.siukatech.poc.react.backend.module.core.data.repository")
//@EntityScan(basePackages = {
//        "com.siukatech.**.core.**.data.entity"
//        , "**.app.**.data.entity"
//})  // "**" means all packages
//@EnableJpaRepositories(value = {
//        "com.siukatech.**.core.**.data.repository"
//        , "**.app.**.data.repository"
//})    // "**" means all packages
@EntityScan(basePackageClasses = {EnableReactBackend.class})
@EnableJpaRepositories(basePackageClasses = {EnableReactBackend.class})
////@Import(StarterEntityRegistrar.class)
public class DataConfig {

    @Bean
    public AuditorAware<String> auditorAware() {
//        return new AuditorAware<String>() {
//            @Override
//            public Optional<String> getCurrentAuditor() {
        return () -> {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String name = (authentication == null ? "NULL" : authentication.getName());
            log.debug("getCurrentAuditor - authentication.getName: [{}]"
                            + ", authentication.getAuthorities.size: [{}]"
//                            + ", authenticationInSc.getName: [{}]"
//                            + ", authenticationInSc.getAuthorities.size: [{}]"
                    , Objects.isNull(authentication)? "NULL" : authentication.getName()
                    , Objects.isNull(authentication)? "NULL" : authentication.getAuthorities().size()
//                    , Objects.isNull(authenticationInSc)? "NULL" : authenticationInSc.getName()
//                    , Objects.isNull(authenticationInSc)? "NULL" : authenticationInSc.getAuthorities().size()
            );
            return Optional.of(name);
        };
//            }
//        };
    }

}
