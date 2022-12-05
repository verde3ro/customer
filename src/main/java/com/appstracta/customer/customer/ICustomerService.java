package com.appstracta.customer.customer;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ICustomerService {

	Flux<Customer> findAll();
	Mono<Customer> findById(String id);
	Mono<Customer> save(Customer customer);

	Mono<Customer> update(Customer customert, String id);

	Mono<Void> delete(String id);

}
