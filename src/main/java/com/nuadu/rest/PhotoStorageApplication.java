package com.nuadu.rest;

import com.nuadu.rest.property.FileStorageProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties({
		FileStorageProperties.class
})
public class PhotoStorageApplication {

	public static void main(String[] args) {
		//adrianremote
		SpringApplication.run(PhotoStorageApplication.class, args);
	}

}
