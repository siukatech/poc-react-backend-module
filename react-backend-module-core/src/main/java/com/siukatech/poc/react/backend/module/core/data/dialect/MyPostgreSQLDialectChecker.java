package com.siukatech.poc.react.backend.module.core.data.dialect;

import jakarta.annotation.PostConstruct;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.boot.Metadata;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.boot.spi.MetadataImplementor;
import org.hibernate.engine.spi.SessionFactoryImplementor;
import org.hibernate.mapping.Table;
import org.springframework.stereotype.Component;

import java.util.Iterator;

@Slf4j
@Component
public class MyPostgreSQLDialectChecker {

    @PersistenceContext
    private EntityManager entityManager;

    @PostConstruct
    public void checkDialect() {

        // Unwrap the EntityManagerFactory to get the SessionFactory
        SessionFactoryImplementor sessionFactory = entityManager
                .getEntityManagerFactory().unwrap(SessionFactoryImplementor.class);
        String dialect = sessionFactory.getJdbcServices().getDialect().toString();
        log.debug("checkDialect - Dialect: " + dialect);

        // Build the ServiceRegistry from SessionFactory
        StandardServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder()
                .applySettings(sessionFactory.getProperties())
                .build();

        // Build Metadata from the ServiceRegistry
        Metadata metadata = new MetadataSources(serviceRegistry).buildMetadata();

        // Iterate through table mappings
        Iterator<Table> tableIterator = ((MetadataImplementor) metadata)
                .collectTableMappings().iterator();

        while (tableIterator.hasNext()) {
            Table table = tableIterator.next();
            log.debug("checkDialect - Table Name: [{}]", table.getName());
        }
    }

}
