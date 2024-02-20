package br.com.duduso.reactive.secure.api.repository;

import br.com.duduso.reactive.secure.api.model.Order;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;

public interface OrderRepository extends ReactiveCrudRepository<Order, Long> {

    @Query("""
            select member.*,
                   orders.order_id,
                   orders.order_name
            from orders
            join member on orders.member_id = member.member_id""")
    Flux<Order> findAllWithMember();

    Flux<Order> findByMember(Long member);

}
