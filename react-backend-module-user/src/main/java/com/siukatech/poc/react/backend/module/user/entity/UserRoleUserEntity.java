package com.siukatech.poc.react.backend.module.user.entity;

import com.siukatech.poc.react.backend.module.core.data.entity.AbstractEntity;
import jakarta.persistence.*;
import lombok.*;

@Data
@EqualsAndHashCode(callSuper = false)
//@Entity(name = "user_role_users")
@Entity
@Table(name = "user_role_users")
public class UserRoleUserEntity extends AbstractEntity<String> {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @ToString.Exclude
    @ManyToOne
    @JoinColumn(name = "user_role_id", referencedColumnName = "user_role_id")
    private UserRoleEntity userRoleEntity;

    @ToString.Exclude
    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "user_id")
    private UserEntity userEntity;

}
