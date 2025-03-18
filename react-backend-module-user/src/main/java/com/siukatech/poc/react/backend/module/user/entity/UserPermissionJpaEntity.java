package com.siukatech.poc.react.backend.module.user.entity;

import com.siukatech.poc.react.backend.module.core.data.entity.AbstractEntity;
import jakarta.persistence.*;
import lombok.*;

/**
 * Add ToString.Exclude to prevent circular reference between ToString.
 *
 * Reference:
 * https://stackoverflow.com/a/54654008
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Entity
@Table(name = "user_permissions")
@NamedEntityGraph(name = "UserPermissionJpaEntity.basic"
    , attributeNodes = {
        @NamedAttributeNode(value = "userRoleEntity")
        , @NamedAttributeNode(value = "applicationEntity")
        , @NamedAttributeNode(value = "appResourceEntity")
})
public class UserPermissionJpaEntity extends AbstractEntity<String> {

    @Id
    private String id;

    @Column(name = "access_right")
    private String accessRight;

    @ToString.Exclude
    @ManyToOne
    @JoinColumn(name = "user_role_id", referencedColumnName = "user_role_id")
    private UserRoleEntity userRoleEntity;

    @ToString.Exclude
    @ManyToOne
    @JoinColumn(name = "application_id", referencedColumnName = "application_id")
    private ApplicationEntity applicationEntity;

    @ToString.Exclude
    @ManyToOne
    @JoinColumn(name = "app_resource_id", referencedColumnName = "app_resource_id")
    private AppResourceEntity appResourceEntity;

}
