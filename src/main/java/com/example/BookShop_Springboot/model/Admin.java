package com.example.BookShop_Springboot.model;

import java.util.Collection;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "users")
public class Admin {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "user_ID")
    @ToString.Exclude
    private long id;

    @Column(name = "username")
    @ToString.Exclude
    private String username;

    @Column(name = "phone")
    @ToString.Exclude
    private String phone;

    @Column(name = "address")
    @ToString.Exclude
    private String address;

    @Column(name = "email")
    @ToString.Exclude
    private String email;

    @Column(name = "status")
    @ToString.Exclude
    private boolean status;

    @Column(name = "passwordHash")
    @ToString.Exclude
    private String password;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "fk_user_role", referencedColumnName = "role_id")
    @ToString.Exclude
    private Role role;

    @OneToMany(mappedBy = "adminCreate", cascade = CascadeType.ALL)
    @ToString.Exclude
    private Collection<Receipt> receipts;
}
