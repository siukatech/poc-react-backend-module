package com.siukatech.poc.react.backend.module.quartz.config;


import com.siukatech.poc.react.backend.module.quartz.EnableQuartzSupport;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Slf4j
@Configuration
//@ComponentScan(value = {"com.siukatech.poc.react.backend.module.quartz"})
@ComponentScan(basePackageClasses = {EnableQuartzSupport.class})
//@EntityScan(basePackages = {"com.siukatech.poc.react.backend.module.quartz.entity"})  // "**" means all packages
//@EnableJpaRepositories("com.siukatech.poc.react.backend.module.quartz.repository")    // "**" means all packages
@EntityScan(basePackageClasses = {EnableQuartzSupport.class})
@EnableJpaRepositories(basePackageClasses = {EnableQuartzSupport.class})
public class QuartzSupportConfig {
}
