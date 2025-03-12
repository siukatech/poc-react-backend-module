package com.siukatech.poc.react.backend.module.user;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceTransactionManagerAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

@SpringBootApplication(
        exclude = {
                DataSourceAutoConfiguration.class
                , DataSourceTransactionManagerAutoConfiguration.class
                , HibernateJpaAutoConfiguration.class
                , SecurityAutoConfiguration.class
        }
)
public class ReactBackendApp {

    public static void main(String[] args) {
        SpringApplication.run(com.siukatech.poc.react.backend.module.core.ReactBackendApp.class, args);
    }

}
