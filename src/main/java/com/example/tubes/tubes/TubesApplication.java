package com.example.tubes.tubes;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories("com.example.tubes.repository")
@EntityScan("com.example.tubes.model")
@ComponentScan(basePackages = {
	"com.example.tubes.controller",
	"com.example.tubes.model",
	"com.example.tubes.repository",
})
public class TubesApplication {

	public static void main(String[] args) {
		SpringApplication.run(TubesApplication.class, args);
	}

}
