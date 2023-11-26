package com.example.BookShop_Springboot.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "permissions")
public class Permission {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "permission_id")
    private long id;

    @Size(min = 0, max = 50, message = "name must be between than 0 to 50 letters")
    @Column(name = "name")
    private String name;

    @Column(name = "status")
    private boolean status;

    // @ManyToMany(fetch = FetchType.LAZY,
    // cascade = {
    //     CascadeType.PERSIST,
    //     CascadeType.MERGE
    // },
    // mappedBy = "permissions")
    // private Collection<Role> roles;
}
