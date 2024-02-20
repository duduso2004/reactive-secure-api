package br.com.duduso.reactive.secure.api.repository;

import br.com.duduso.reactive.secure.api.model.Customer;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.r2dbc.DataR2dbcTest;

import static org.assertj.core.api.Assertions.assertThat;

@DataR2dbcTest
@TestMethodOrder(OrderAnnotation.class)
class CustomerRepositoryTest {

    @Autowired
    CustomerRepository repository;

    @Test
    @Order(1)
    void testCustomerInsert() {
        final var customer = this.repository.save(Customer.builder()
                .firstName("José")
                .lastName("Inocêncio")
                .build())
                .block();
        assertThat(customer).isNotNull();
    }

    @Test
    @Order(2)
    void testCustomerUpdate() {
        final var originalCustomer = this.repository.findById(1L).block();
        assertThat(originalCustomer).isNotNull();
        this.repository.save(originalCustomer.toBuilder()
                .firstName("João")
                .lastName("Pedro")
                .build())
                .block();
        final var changedCustomer = this.repository.findById(1L).block();
        assertThat(changedCustomer).isNotNull();
        assertThat(changedCustomer.getFirstName()).isEqualTo("João");
        assertThat(changedCustomer.getLastName()).isEqualTo("Pedro");
    }

    @Test
    @Order(3)
    void testCustomerFindAll() {
        final var customers = this.repository.findAll().collectList().block();
        assertThat(customers).isNotEmpty();
    }

    @Test
    @Order(4)
    void testCustomerFindByLastName() {
        final var customers = this.repository.findByLastName("Pedro").collectList().block();
        assertThat(customers).isNotEmpty();
    }

    @Test
    @Order(5)
    void testCustomerDelete() {
        final var customer = this.repository.findById(1L).block();
        assertThat(customer).isNotNull();
        this.repository.delete(customer).block();
        assertThat(this.repository.findById(1L).block()).isNull();
    }

}
