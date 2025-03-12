package com.siukatech.poc.react.backend.module.user.entity;

import com.siukatech.poc.react.backend.module.core.data.entity.AbstractEntity;
import jakarta.persistence.*;
import lombok.*;

@Data
//@Entity(name = "app_resources")
@Entity
@Table(name = "app_resources")
@NamedEntityGraph(name = "AppResourceEntity.basic"
    , attributeNodes = {
        @NamedAttributeNode(value = "applicationEntity")
//        , @NamedAttributeNode(value = "userPermissionJpaEntities")
})
public class AppResourceEntity extends AbstractEntity<String> {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(name = "app_resource_id")
    protected String appResourceId;

    @Column(name = "name")
    private String name;

//    @Column(name = "access_right")
//    private String accessRight;

    @ToString.Exclude
    @ManyToOne
    @JoinColumn(name = "application_id", referencedColumnName = "application_id")
    private ApplicationEntity applicationEntity;

//    @ToString.Exclude
//    @OneToMany(mappedBy = "appResourceEntity", fetch = FetchType.EAGER)
//    private List<UserPermissionJpaEntity> userPermissionJpaEntities;

}
