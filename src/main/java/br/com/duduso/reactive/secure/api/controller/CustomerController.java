package br.com.duduso.reactive.secure.api.controller;

import br.com.duduso.reactive.secure.api.model.Customer;
import br.com.duduso.reactive.secure.api.repository.CustomerRepository;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
@RequestMapping("/protected/customer")
@SecurityRequirement(name = "bearerAuth")
public class CustomerController {

    private final CustomerRepository repository;

    @GetMapping
    public Flux<Customer> findAll() {
        return this.repository.findAll();
    }

    @GetMapping("/{id}")
    public Mono<Customer> findById(@PathVariable Long id) {
        return this.repository.findById(id);
    }

    @PostMapping
    public Mono<Customer> save(@RequestBody Customer customer) {
        return this.repository.save(customer);
    }

    @DeleteMapping("/{id}")
    public Mono<Void> delete(@PathVariable Long id) {
        return this.repository.deleteById(id);
    }

}
