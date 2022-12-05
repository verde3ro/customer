package com.appstracta.customer.customer;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.support.WebExchangeBindException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/customer")
@RequiredArgsConstructor
public class CustomerController {
	private final ICustomerService service;

	@GetMapping
	public Mono<ResponseEntity<Flux<Customer>>> findAll() {
		return Mono.just(ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(service.findAll()));
	}

	@GetMapping("/{id}")
	public Mono<ResponseEntity<Customer>> findById(@PathVariable String id) {
		return service.
				findById(id)
				.map(p -> ResponseEntity
						.ok()
						.contentType(MediaType.APPLICATION_JSON)
						.body(p))
				.defaultIfEmpty(ResponseEntity.notFound().build());
	}

	@PostMapping
	public Mono<ResponseEntity<Map<String, Object>>> save(@RequestBody Mono<Customer> customerMono) { // Mono pra obtener los errores
		Map<String, Object> respuesta = new HashMap<>();

		return customerMono
				.flatMap(customer -> service.save(customer)
						.map(c -> {
							respuesta.put("cliente", c);
							respuesta.put("mensaje", "Cliente creado con éxito");
							respuesta.put("timestamp", new Date());

							return ResponseEntity
									.created(URI.create("/customer/".concat(c.getId())))
									.contentType(MediaType.APPLICATION_JSON)
									.body(respuesta);
						})).onErrorResume(t -> Mono.just(t).cast(WebExchangeBindException.class)
						.flatMap(e -> Mono.just(e.getFieldErrors()))
						.flatMapMany(Flux::fromIterable)
						.map(fieldError -> "El campo " + fieldError.getField() + " " + fieldError.getDefaultMessage())
						.collectList()
						.flatMap(list -> {
							respuesta.put("errors", list);
							respuesta.put("timestamp", new Date());
							respuesta.put("status", HttpStatus.BAD_REQUEST.value());
							return Mono.just(ResponseEntity.badRequest().body(respuesta));
						}));
	}

	@PutMapping("/{id}")
	public Mono<ResponseEntity<Customer>> update(@RequestBody Customer customer, @PathVariable String id) {
		return service.update(customer, id)
				.map(p -> ResponseEntity
						.created(URI.create("/customer/".concat(p.getId())))
						.contentType(MediaType.APPLICATION_JSON)
						.body(p))
				.defaultIfEmpty(ResponseEntity.notFound().build());
	}

	@DeleteMapping("/{id}")
	public Mono<ResponseEntity<Void>> delete(@PathVariable String id) {
		return service.delete(id)
				.then(Mono.just(new ResponseEntity<Void>(HttpStatus.NO_CONTENT)))
				.defaultIfEmpty(new ResponseEntity<>(HttpStatus.NOT_FOUND));
	}

}
