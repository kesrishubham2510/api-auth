package com.myreflectionthoughts.auth.datamodel.entity;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.myreflectionthoughts.auth.datamodel.role.UserRole;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.UuidGenerator;

import java.util.List;


@Data
@Entity(name="users")
@Table(name="users", schema = "letschat",
        indexes = {
                @Index(name="idx_users_email", columnList = "email", unique = true),
                @Index(name="idx_users_username", columnList = "user_name", unique = true)
        }
)
@JsonIdentityInfo(
        generator = ObjectIdGenerators.PropertyGenerator.class,
        property = "userId"
)
public class User {

    @Id
    @UuidGenerator
    @Column(name = "user_id")
    private String userId;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name="user_name")
    private String username;

    @Column(name="email")
    private String email;

    @Column(name = "password")
    private String password;

    @Column(name = "role")
    @Enumerated(EnumType.STRING)
    private UserRole role;

    @Column(name = "joined")
    private String joined;

    @Column(name="emailverified")
    private boolean emailVerified;
}

