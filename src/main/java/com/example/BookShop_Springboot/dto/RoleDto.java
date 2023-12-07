package com.example.BookShop_Springboot.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RoleDto {
	private long roleId;
	private String description;
	private String name;
	private boolean status;
}
