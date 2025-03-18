package com.siukatech.poc.react.backend.module.user.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;


@Data
@EqualsAndHashCode(callSuper = false)
//@Entity(name = "users")
@Entity
@Table(name = "users")
@NamedEntityGraph(name = "UserEntity.basic"
    , attributeNodes = {
        @NamedAttributeNode(value = "userRoleUserEntities"
            , subgraph = "UserEntity.userRoleUserEntities")
    }
    , subgraphs = {
        @NamedSubgraph(name = "UserEntity.userRoleUserEntities", attributeNodes = {
                @NamedAttributeNode(value = "userRoleEntity"
                        , subgraph = "UserEntity.userRoleUserEntities.userRoleEntity"
                )
        })
////        , @NamedSubgraph(name = "UserEntity.userRoleUserEntities.userRoleEntity"
////            , attributeNodes = {
////                @NamedAttributeNode(value = "userPermissionJpaEntities")
////        })
//        , @NamedSubgraph(name = "UserEntity.userRoleUserEntities.userRoleEntity"
//            , attributeNodes = {
//                @NamedAttributeNode(value = "userPermissionJpaEntities"
//                    , subgraph = "UserEntity.userRoleUserEntities.userRoleEntity.userPermissionJpaEntities")
//        })
//        , @NamedSubgraph(name = "UserEntity.userRoleUserEntities.userRoleEntity.userPermissionJpaEntities"
//            , attributeNodes = {
//                @NamedAttributeNode(value = "applicationEntity"
//                    , subgraph = "UserEntity.userRoleUserEntities.userRoleEntity.userPermissionJpaEntities.applicationEntity")
//                , @NamedAttributeNode(value = "appResourceEntity")
//        })
//        , @NamedSubgraph(name = "UserEntity.userRoleUserEntities.userRoleEntity.userPermissionJpaEntities.applicationEntity"
//            , attributeNodes = {
//                @NamedAttributeNode(value = "appResourceEntities")
//        })
    }
)
public class UserEntity extends AbstractUserEntity {

    @ToString.Exclude
    @OneToMany(mappedBy = "userEntity", fetch = FetchType.EAGER)
    private List<UserRoleUserEntity> userRoleUserEntities;

}
