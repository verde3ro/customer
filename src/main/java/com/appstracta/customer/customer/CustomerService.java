package com.appstracta.customer.customer;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.beans.BeanUtils;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
@Service
public class CustomerService implements ICustomerService {

	private final ICustomerRepository repository;

	@Override
	public Flux<Customer> findAll() {
		return repository.findAll();
	}

	@Override
	public Mono<Customer> findById(String id) {
		return repository.findById(id);
	}

	@Override
	public Mono<Customer> save(Customer customer) {
		return repository.save(customer);
	}

	@Override
	public Mono<Customer> update(Customer customert, String id) {
		return repository.findById(id).
				flatMap(c -> {
					BeanUtils.copyProperties(customert, c);

					return repository.save(c);
				});
	}

	@Override
	public Mono<Void> delete(String id) {
		return repository.findById(id).flatMap(repository::delete);
	}

}
