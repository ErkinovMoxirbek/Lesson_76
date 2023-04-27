package com.example.entity;

import com.example.enums.GeneralStatus;
import com.example.enums.ProfileRole;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
@Entity
@Table(name = "profile")
@Getter @Setter
public class ProfileEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(name = "name")
    private String name;
    @Column(name = "surname")
    private String surname;
    @Column(name = "email")
    private String email;
    @Column(name = "phone")
    private String phone;
    @Column(name = "password")
    private String password;
    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private GeneralStatus status = GeneralStatus.ACTIVED;
    @Enumerated(EnumType.STRING)
    @Column(name = "role")
    private ProfileRole role;
    @Column(name = "visible")
    private Boolean visible = Boolean.TRUE;
    @Column(name = "created_date")
    private LocalDateTime createdDate = LocalDateTime.now();
    // photo_id
}
