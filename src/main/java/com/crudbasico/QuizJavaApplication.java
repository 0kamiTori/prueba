package com.crudbasico;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.persistence.autoconfigure.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories(basePackages = "repository") // <-- Obliga a buscar los repositorios
@EntityScan(basePackages = "model")

public class QuizJavaApplication {

	public static void main(String[] args) {
		SpringApplication.run(QuizJavaApplication.class, args);
	}

}
