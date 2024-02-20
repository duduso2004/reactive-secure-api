package br.com.duduso.reactive.secure.api.repository;

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.r2dbc.DataR2dbcTest;

import static org.assertj.core.api.Assertions.assertThat;

@DataR2dbcTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class OrderRepositoryTest {

    @Autowired
    OrderRepository repository;

    @Test
    void findOrdersWithAllMemberTest() {

        final var orders = this.repository.findAllWithMember().collectList().block();

        assertThat(orders).isNotEmpty();
        assertThat(orders).hasSize(3);

    }

    @Test
    void findOrdersByMemberTest() {

        final var orders = this.repository.findByMember(1L).collectList().block();

        assertThat(orders).isNotEmpty();
        assertThat(orders).hasSize(1);

    }

}
