package com.siukatech.poc.react.backend.module.core.data.entity;

import com.siukatech.poc.react.backend.module.core.data.listener.AbstractEntityToPersistListener;
import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.Version;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * // here is required to use @MappedSupperclass for sharing the base class members
 */

@Slf4j
@Getter
@Setter
@ToString
//@EqualsAndHashCode(callSuper = true)
//@Entity
//@Inheritance
@MappedSuperclass
@EntityListeners(value = {AuditingEntityListener.class, AbstractEntityToPersistListener.class})
public abstract class AbstractEntity<T> implements Serializable {

//    @Transient
//    protected final Logger log = LoggerFactory.getLogger(this.getClass());

    //    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    protected T id;
    public abstract T getId();

    public abstract void setId(T id);

    @CreatedBy
    @Column(name = "created_by")
    protected String createdBy;
    @CreatedDate
    @Column(name = "created_datetime")
    protected LocalDateTime createdDatetime;
    @LastModifiedBy
    @Column(name = "last_modified_by")
    protected String lastModifiedBy;
    @LastModifiedDate
    @Column(name = "last_modified_datetime")
    protected LocalDateTime lastModifiedDatetime;

    /**
     * This field cannot be null, if null, the repository will treat as "CREATE" action.
     */
    @Version
//    @NotNull(message = "Version No cannot be NULL!!!")
    protected Long versionNo;


    // This can be handled by AbstractEntityToPersistListener.class by adding to @EntityListeners
//    @PreUpdate
//    protected void onSavePreUpdate() {
//        log.debug("onSavePreUpdate - getId: [" + this.getId()
//                + "], getVersionNo: [" + this.getVersionNo()
//                + "]");
//        if (this.getVersionNo() == null) {
//            throw new IllegalArgumentException("VersionNo cannot null be null for " + this.getClass().getName() + ".id#" + this.getId() + ", onSavePreUpdate");
//        }
//    }
//
//    @PrePersist
//    protected void onSavePrePersist() {
//        log.debug("onSavePrePersist - getId: [" + this.getId()
//                + "], getVersionNo: [" + this.getVersionNo()
//                + "]");
//        if (this.getId() != null && this.getVersionNo() == null) {
//            // since the version is null, it treats "CREATE"
//            String message = "VersionNo cannot null be null if " + this.getClass().getName() + ".id#" + this.getId() + " exists, onSavePrePersist";
//            throw new IllegalArgumentException(message);
//        }
//    }

}
