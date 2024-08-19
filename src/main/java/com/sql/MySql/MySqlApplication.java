package com.sql.MySql;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories(basePackages = "com.sql.MySql.repositories")
public class MySqlApplication {

	public static void main(String[] args) {
		SpringApplication.run(MySqlApplication.class, args);
	}

}
