package com.siukatech.poc.react.backend.module.user.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.jpa.repository.EntityGraph;

@Data
@EqualsAndHashCode(callSuper = false)
@Entity
@Table(name = "user_permissions")
public class UserPermissionEntity {

    @Id
    private String id;

    @Column(name = "access_right", insertable = false, updatable = false)
    private String accessRight;

    @Column(name = "user_id", insertable = false, updatable = false)
    private String userId;

    @Column(name = "user_role_id", insertable = false, updatable = false)
    private String userRoleId;

    @Column(name = "application_id", insertable = false, updatable = false)
    private String applicationId;

    @Column(name = "app_resource_id", insertable = false, updatable = false)
    private String appResourceId;

//    @ToString.Exclude
//    @ManyToOne
//    @JoinColumn(name = "user_id", referencedColumnName = "id")
//    private UserEntity userEntity;
//
//    @ToString.Exclude
//    @ManyToOne
//    @JoinColumn(name = "user_role_id", referencedColumnName = "id")
//    private UserRoleEntity userRoleEntity;
//
//    @ToString.Exclude
//    @ManyToOne
//    @JoinColumn(name = "application_id", referencedColumnName = "id")
//    private ApplicationEntity applicationEntity;
//
//    @ToString.Exclude
//    @ManyToOne
//    @JoinColumn(name = "app_resource_id", referencedColumnName = "id")
//    private AppResourceEntity appResourceEntity;

}
