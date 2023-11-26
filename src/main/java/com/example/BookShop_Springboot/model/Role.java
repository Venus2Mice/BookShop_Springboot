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
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "roles")
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "role_id")
    private long id;

    @Size(min = 0, max = 50, message = "name must be between than 0 to 50 letters")
    @Column(name = "name")
    private String name;

    @Size(min = 0, max = 50, message = "description must be between than 0 to 50 letters")
    @Column(name = "description")
    private String description;

    @Column(name = "status")
    private boolean status;
    
    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(
        name = "roles_permissions", 
        joinColumns = {
            @JoinColumn(name = "role_id" , referencedColumnName = "role_id")
        }, 
        inverseJoinColumns = {
            @JoinColumn(name = "permission_id" , referencedColumnName = "permission_id")
    }
    )
    private Collection<Permission> permissions;

    @OneToMany(mappedBy = "role", cascade = CascadeType.ALL)
    private Collection<Admin> admins;
}
