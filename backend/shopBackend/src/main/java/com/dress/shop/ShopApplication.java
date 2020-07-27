package com.dress.shop;

import com.cloudinary.Cloudinary;
import org.keycloak.adapters.KeycloakDeployment;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.HashMap;
import java.util.Map;

@SpringBootApplication
public class ShopApplication {

	@Value("${cloudinary.cloud_name}")
	private String cloudName;

	@Value("${cloudinary.api_key}")
	private String apiKey;

	@Value("${cloudinary.api_secret}")
	private String apiSecret;

	public static void main(String[] args) {
		SpringApplication.run(ShopApplication.class, args);
	}

	@Bean
	public Cloudinary cloudinaryConfig() {
		Cloudinary cloudinary = null;
		Map config = new HashMap();
		config.put("cloud_name", cloudName);
		config.put("api_key", apiKey);
		config.put("api_secret", apiSecret);
		cloudinary = new Cloudinary(config);
		return cloudinary;
	}

	@Bean
	public KeycloakDeployment keycloakDeployment() {
		return new KeycloakDeployment();
	}

//	@Bean
//	public KeycloakSpringBootConfigResolver keycloakConfigResolver() {
//		return new KeycloakSpringBootConfigResolver();
//	}
}
