package com.siukatech.poc.react.backend.module.user.entity;

import com.siukatech.poc.react.backend.module.core.data.entity.AbstractEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper = false)
//@Entity(name = "user_roles")
@Entity
@Table(name = "user_roles")
@NamedEntityGraph(name = "UserRoleEntity.basic"
    , attributeNodes = {
        @NamedAttributeNode(value = "userRoleUserEntities"
                , subgraph = "UserRoleEntity.userRoleUserEntities")
    }
    , subgraphs = {
        @NamedSubgraph(name = "UserRoleEntity.userRoleUserEntities", attributeNodes = {
                @NamedAttributeNode(value = "userEntity"
                    , subgraph = "UserRoleEntity.userRoleUserEntities.userEntity"
                )
        })
    }
)
public class UserRoleEntity extends AbstractEntity<String> {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(name = "user_role_id")
    protected String userRoleId;

    @Column(name = "name")
    private String name;

    // FIXME, Remove OneToMany
    @ToString.Exclude
    @OneToMany(mappedBy = "userRoleEntity", fetch = FetchType.EAGER)
    private List<UserRoleUserEntity> userRoleUserEntities;

    // FIXME, Remove OneToMany
    @ToString.Exclude
    @OneToMany(mappedBy = "userRoleEntity", fetch = FetchType.EAGER)
    private List<UserPermissionJpaEntity> userPermissionJpaEntities;

}
