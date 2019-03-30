/**
 * @Author iyboklee (iyboklee@gmail.com)
 */
package com.github.iyboklee;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.github.iyboklee.core.model.Customer;
import com.github.iyboklee.core.service.CustomerService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@SpringBootApplication
public class Application implements CommandLineRunner {

	@Autowired private CustomerService customerService;

	@Override
	public void run(String... args) throws Exception {
		Customer customer = customerService.findOne(1L);
		Customer customerWithReadOnly = customerService.findOneWithReadOnly(1L);
		log.info("{}", customer);
		log.info("READ ONLY --> {}", customerWithReadOnly);
	}

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

}