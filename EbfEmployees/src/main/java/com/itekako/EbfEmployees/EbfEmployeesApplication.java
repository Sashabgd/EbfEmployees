package com.itekako.EbfEmployees;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.retry.annotation.EnableRetry;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableTransactionManagement
@EnableRetry
public class EbfEmployeesApplication {

	public static void main(String[] args) {
		SpringApplication.run(EbfEmployeesApplication.class, args);
	}

}
