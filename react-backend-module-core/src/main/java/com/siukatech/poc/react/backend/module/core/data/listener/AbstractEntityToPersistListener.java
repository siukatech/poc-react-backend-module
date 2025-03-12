package com.siukatech.poc.react.backend.module.core.data.listener;


import com.siukatech.poc.react.backend.module.core.data.entity.AbstractEntity;
import jakarta.persistence.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class AbstractEntityToPersistListener {

    @PrePersist
    protected void onSavePrePersist(final AbstractEntity<?> abstractEntity) {
        log.debug("onSavePrePersist - abstractEntity.getClass().getName: [{}"
                + "], abstractEntity.getId: [{}"
                + "], abstractEntity.getVersionNo: [{}"
                + "]"
                , abstractEntity.getClass().getName()
                , abstractEntity.getId()
                , abstractEntity.getVersionNo());
        if (abstractEntity.getId() != null && abstractEntity.getVersionNo() == null) {
            // since the version is null, it treats "CREATE"
            String message = "VersionNo cannot null be null if abstractEntity.id#" + abstractEntity.getId() + " exists, onSavePrePersist";
            throw new IllegalArgumentException(message);
        }
    }

    @PostPersist
    protected void onSavePostPersist(final AbstractEntity<?> abstractEntity) {
        log.debug("onSavePostPersist - abstractEntity.getClass().getName: [{}"
                        + "], abstractEntity.getId: [{}"
                        + "], abstractEntity.getVersionNo: [{}"
                        + "]"
                , abstractEntity.getClass().getName()
                , abstractEntity.getId()
                , abstractEntity.getVersionNo());
    }

    @PreUpdate
    protected void onSavePreUpdate(final AbstractEntity<?> abstractEntity) {
        log.debug("onSavePreUpdate - abstractEntity.getClass().getName: [{}"
                        + "], abstractEntity.getId: [{}"
                        + "], abstractEntity.getVersionNo: [{}"
                        + "]"
                , abstractEntity.getClass().getName()
                , abstractEntity.getId()
                , abstractEntity.getVersionNo());
        if (abstractEntity.getVersionNo() == null) {
            throw new IllegalArgumentException("VersionNo cannot null be null for " + abstractEntity.getClass().getName() + ".id#" + abstractEntity.getId() + ", onSavePreUpdate");
        }
    }

    @PostUpdate
    protected void onSavePostUpdate(final AbstractEntity<?> abstractEntity) {
        log.debug("onSavePostUpdate - abstractEntity.getClass().getName: [{}"
                        + "], abstractEntity.getId: [{}"
                        + "], abstractEntity.getVersionNo: [{}"
                        + "]"
                , abstractEntity.getClass().getName()
                , abstractEntity.getId()
                , abstractEntity.getVersionNo());
    }

    @PreRemove
    protected void onSavePreRemove(final AbstractEntity<?> abstractEntity) {
        log.debug("onSavePreRemove - abstractEntity.getClass().getName: [{}"
                        + "], abstractEntity.getId: [{}"
                        + "], abstractEntity.getVersionNo: [{}"
                        + "]"
                , abstractEntity.getClass().getName()
                , abstractEntity.getId()
                , abstractEntity.getVersionNo());
    }

    @PostRemove
    protected void onSavePostRemove(final AbstractEntity<?> abstractEntity) {
        log.debug("onSavePostRemove - abstractEntity.getClass().getName: [{}"
                        + "], abstractEntity.getId: [{}"
                        + "], abstractEntity.getVersionNo: [{}"
                        + "]"
                , abstractEntity.getClass().getName()
                , abstractEntity.getId()
                , abstractEntity.getVersionNo());
    }

    @PostLoad
    protected void onGetPostLoad(final AbstractEntity<?> abstractEntity) {
        log.debug("onGetPostLoad - abstractEntity.getClass().getName: [{}"
                        + "], abstractEntity.getId: [{}"
                        + "], abstractEntity.getVersionNo: [{}"
                        + "]"
                , abstractEntity.getClass().getName()
                , abstractEntity.getId()
                , abstractEntity.getVersionNo());
    }

}
