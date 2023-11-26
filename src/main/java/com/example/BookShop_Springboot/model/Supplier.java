package com.example.BookShop_Springboot.model;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "suppliers", uniqueConstraints = @UniqueConstraint(columnNames = "name"))
public class Supplier {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "supplier_id")
    private Long id;
    private String name;
    private String address;
    private String phoneNumber;
    private String email;
    private boolean is_deleted;
    private boolean is_activated;

    public Supplier(String name) {
        this.name = name;
        this.is_activated = true;
        this.is_deleted = false;
    }
}
