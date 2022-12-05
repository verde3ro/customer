package com.appstracta.customer.customer;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface ICustomerRepository extends ReactiveMongoRepository<Customer, String> {
}
