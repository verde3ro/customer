package com.appstracta.customer;

import com.appstracta.customer.customer.Customer;
import com.appstracta.customer.customer.ICustomerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import reactor.core.publisher.Flux;

@Slf4j
@SpringBootApplication
@RequiredArgsConstructor
public class CustomerApplication implements CommandLineRunner {
	private final ICustomerService service;
	private final ReactiveMongoTemplate template;

	public static void main(String[] args) {
		SpringApplication.run(CustomerApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		template.dropCollection("customers").subscribe();

		Flux.just(
				new Customer("Raul", "Verde", "0000000000", "personal"),
				new Customer("Alejandro", "Martínez", "0000000001", "empresarial")
		).flatMap(service::save).subscribe(c -> log.info("Insert customer : " + c.toString()));
	}

}
