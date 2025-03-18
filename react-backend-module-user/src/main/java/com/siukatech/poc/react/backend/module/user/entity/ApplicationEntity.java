package com.siukatech.poc.react.backend.module.user.entity;

import com.siukatech.poc.react.backend.module.core.data.entity.AbstractEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper = false)
//@Entity(name = "applications")
@Entity
@Table(name = "applications")
@NamedEntityGraphs(value = {
    @NamedEntityGraph(name = "ApplicationEntity.appResourceEntities"
        , attributeNodes = {
            @NamedAttributeNode(value = "appResourceEntities")
//        , @NamedAttributeNode(value = "userPermissionJpaEntities")
    })
    , @NamedEntityGraph(name = "ApplicationEntity.userPermissionJpaEntities"
        , attributeNodes = {
            @NamedAttributeNode(value = "userPermissionJpaEntities")
    })
})
public class ApplicationEntity extends AbstractEntity<String> {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(name = "application_id")
    protected String applicationId;

    @Column(name = "name")
    private String name;

    @ToString.Exclude
    @OneToMany(mappedBy = "applicationEntity"
//            , fetch = FetchType.EAGER
    )
//    @Fetch(FetchMode.SUBSELECT)
    private List<AppResourceEntity> appResourceEntities;

    @ToString.Exclude
    @OneToMany(mappedBy = "applicationEntity", fetch = FetchType.EAGER)
    private List<UserPermissionJpaEntity> userPermissionJpaEntities;

}
