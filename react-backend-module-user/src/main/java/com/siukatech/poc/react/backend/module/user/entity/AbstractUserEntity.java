package com.siukatech.poc.react.backend.module.user.entity;

import com.siukatech.poc.react.backend.module.core.data.entity.AbstractEntity;
import jakarta.persistence.*;
import lombok.Data;

@Data
@MappedSuperclass
public abstract class AbstractUserEntity extends AbstractEntity<String> {

    @Id
////    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    @GeneratedValue(strategy = GenerationType.AUTO)
    @GeneratedValue(strategy = GenerationType.UUID)
    protected String id;

    @Column(name = "user_id")
    protected String userId;

    @Column
    protected String name;

    @Column(name = "public_key")
    protected String publicKey;

    @Column(name = "private_key")
    protected String privateKey;

}
