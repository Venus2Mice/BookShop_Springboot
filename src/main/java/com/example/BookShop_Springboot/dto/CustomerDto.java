package com.example.BookShop_Springboot.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CustomerDto {
    @Size(min = 5, max = 20, message = "Username must be 5 to 20 letters")
    private String username;

    @Size(min = 10, max = 10, message = "Phone number must be 10 number")
    private String phone;

    @NotBlank(message = "Email is required")
    private String email;

    @Size(min = 5, max = 20, message = "Password must be between than 5 to 20 letters")
    private String password;


    private String address;
    private String confirmPassword;
    private String image;
}
