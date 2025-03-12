package com.siukatech.poc.react.backend.module.core.global.config;

import com.siukatech.poc.react.backend.module.core.business.form.AbstractForm;
import com.siukatech.poc.react.backend.module.core.data.entity.AbstractEntity;
import jakarta.annotation.PostConstruct;
import jakarta.persistence.Entity;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ClassUtils;
import org.modelmapper.ConfigurationException;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.reflections.Reflections;
import org.springframework.context.annotation.Configuration;

import java.lang.reflect.InvocationTargetException;
import java.util.Set;

@Slf4j
@Configuration
public class PostAppConfig {

    private ModelMapper modelMapper;

    public PostAppConfig(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    // DO NOT do this here
    @PostConstruct
    public void configModelMapper() {
        log.debug("configModelMapper - react-backend-module!!!");
        // https://www.baeldung.com/java-scan-annotations-runtime
//        String packageName = this.getClass().getPackageName();
        String packageName = "com.siukatech";
//        String packageName = (new UserEntity()).getClass().getPackageName();
        Reflections reflections = new Reflections(packageName);
        Set<Class<?>> entityTypes = reflections.getTypesAnnotatedWith(Entity.class);
        log.debug("configModelMapper - packageName: [{}], entityTypes.size: [{}], entityTypes: [{}]", packageName, entityTypes.size(), entityTypes);
        entityTypes.forEach(entityType -> {
            String typeName = entityType.getName();
            String simpleName = entityType.getSimpleName();
            String formTypeName = null;
            formTypeName = typeName.replaceFirst("Entity", "Form")
                    .replaceFirst("data\\.entity", "business\\.form");
//            formTypeName = simpleName.replaceFirst("Entity", "Form");
            log.debug("configModelMapper - entityTypes.forEach - typeName: [{}], simpleName: [{}], formTypeName: [{}]"
                    , typeName, simpleName, formTypeName);
            if (formTypeName.endsWith("Form")) {
                try {
                    Class<?> formType = ClassUtils.getClass(formTypeName);
                    AbstractEntity<?> entityObj = (AbstractEntity) entityType.getDeclaredConstructor().newInstance();
                    AbstractForm<?> formObj = (AbstractForm) formType.getDeclaredConstructor().newInstance();
                    registerPropertyMap(formObj, entityObj);
                } catch (ClassNotFoundException e) {
                    log.error("configModelMapper - ClassNotFoundException - e.getMessage: " + e.getMessage());
                } catch (InvocationTargetException
                         | InstantiationException
                         | IllegalAccessException
                         | NoSuchMethodException
                        e) {
                    //throw new RuntimeException(e);
                    // do nothing
                    log.error("configModelMapper - e.getMessage: " + e.getMessage(), e);
                }
            }
        });

    }

    private <F extends AbstractForm, E extends AbstractEntity> void registerPropertyMap(
            F abstractForm, E abstractEntity) {
//        ModelMapper modelMapper = modelMapper();
        log.debug("registerPropertyMap - F-abstractForm.getClass().getName: [{}]"
                        + ", E-abstractEntity.getClass().getName: [{}]"
                , abstractForm.getClass().getName()
                , abstractEntity.getClass().getName()
        );
        try {
            boolean isContained = modelMapper.getTypeMaps().stream()
                    .filter(typeMap -> {
                        boolean doTypeChecking = false;
//                        doChecking = typeMap.getSourceType().getSimpleName().equals(AbstractForm.class.getSimpleName());
                        doTypeChecking = typeMap.getSourceType().equals(AbstractForm.class);
                        log.debug("registerPropertyMap - getTypeMaps.stream - typeMap.getSourceType().getName: [{}]"
                                        + ", typeMap.getDestinationType().getName: [{}]"
                                        + ", doChecking: [{}]"
                                , typeMap.getSourceType().getName()
                                , typeMap.getDestinationType().getName()
                                , doTypeChecking
                        );
                        return doTypeChecking;
                    })
                    .count() > 0;
            log.debug("registerPropertyMap - modelMapper.getTypeMaps: [{}]"
                            + ", isContained: [{}]"
                    , modelMapper.getTypeMaps()
                    , isContained
            );
            if (!isContained) {
                modelMapper.addMappings(new PropertyMap<F, E>() {
                    protected void configure() {
                        skip().setId(null);
                    }
                });
            }
        } catch (ConfigurationException e) {
            log.error("registerPropertyMap - e.getMessage: " + e.getMessage(), e);
        }
    }

}
