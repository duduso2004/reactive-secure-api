package br.com.duduso.reactive.secure.api.converter;

import br.com.duduso.reactive.secure.api.dto.ChuckNorrisResponseDTO;
import br.com.duduso.reactive.secure.api.dto.ResponseDTO;
import br.com.duduso.reactive.secure.api.model.Customer;
import br.com.duduso.reactive.secure.api.model.Member;
import br.com.duduso.reactive.secure.api.model.Order;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
public class ResponseConverter {

    public Mono<ResponseDTO> toResponseDTO(Customer customer, Member member, Order order, ChuckNorrisResponseDTO chuckNorrisResponse) {
        return Mono.just(ResponseDTO.builder()
                .customer(customer)
                .member(member)
                .order(order)
                .chuckNorrisJoke(chuckNorrisResponse)
                .build());
    }

}
