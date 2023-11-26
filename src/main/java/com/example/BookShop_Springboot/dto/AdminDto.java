package com.example.BookShop_Springboot.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AdminDto {

    @Size(min = 5, max = 20, message = "username must be between than 5 to 20 letters")
    private String username;

    @Size(min = 10, max = 10, message = "phone number must be 10 number")
    private String phone;

    private String address;

    @NotBlank(message = "email is required")
    private String email;

    @Size(min = 5, max = 20, message = "password must be between than 5 to 20 letters")
    private String password;

    private String repeatPassword;
}
