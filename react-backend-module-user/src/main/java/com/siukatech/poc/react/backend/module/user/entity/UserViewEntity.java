package com.siukatech.poc.react.backend.module.user.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(callSuper = false)
//@Entity(name = "users_view")
@Entity
@Table(name = "users_view")
public class UserViewEntity extends AbstractUserEntity {

    @Column(name = "current_ts")
    private LocalDateTime dbDatetime;

}
