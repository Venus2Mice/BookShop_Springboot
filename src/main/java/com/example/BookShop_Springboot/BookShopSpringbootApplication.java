package com.example.BookShop_Springboot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication(scanBasePackages = "com.example.BookShop_Springboot.*")
@EnableJpaRepositories(value = "com.example.BookShop_Springboot.repository")
@EntityScan(value = "com.example.BookShop_Springboot.model")
@EnableCaching
public class BookShopSpringbootApplication {

	public static void main(String[] args) {
		SpringApplication.run(BookShopSpringbootApplication.class, args);
	}

}
