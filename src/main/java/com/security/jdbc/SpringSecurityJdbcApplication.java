package com.security.jdbc;

import com.security.jdbc.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Component;

@SpringBootApplication
public class SpringSecurityJdbcApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringSecurityJdbcApplication.class, args);


	}

}
