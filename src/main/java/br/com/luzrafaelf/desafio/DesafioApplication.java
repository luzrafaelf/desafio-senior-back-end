package br.com.luzrafaelf.desafio;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@ComponentScan("br.com.luzrafaelf.desafio")
@EntityScan(basePackages = { "br.com.luzrafaelf.desafio" })
@EnableJpaRepositories(basePackages = { "br.com.luzrafaelf.desafio" })
public class DesafioApplication {

	public static void main(String[] args) {
		SpringApplication.run(DesafioApplication.class, args);
	}

}
