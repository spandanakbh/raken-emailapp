package com.raken.email;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackageClasses = EmailApplication.class)
public class EmailApplication {

	public static void main(String[] args) {
		SpringApplication.run(EmailApplication.class, args);
	}
}
