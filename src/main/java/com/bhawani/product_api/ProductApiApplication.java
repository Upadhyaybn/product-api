package com.bhawani.product_api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

/**
 * Main entry point for Product API.
 */
@EnableJpaAuditing
@SpringBootApplication
public class ProductApiApplication {

	public static void main(String[] args) {

		SpringApplication.run(ProductApiApplication.class, args);
	}

}
