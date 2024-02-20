package br.com.duduso.reactive.secure.api.service;

import br.com.duduso.reactive.secure.api.client.ChuckNorrisClient;
import br.com.duduso.reactive.secure.api.converter.ResponseConverter;
import br.com.duduso.reactive.secure.api.dto.ResponseDTO;
import br.com.duduso.reactive.secure.api.repository.CustomerRepository;
import br.com.duduso.reactive.secure.api.repository.MemberRepository;
import br.com.duduso.reactive.secure.api.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class ResponseService {

    private final OrderRepository orderRepository;
    private final MemberRepository memberRepository;
    private final ResponseConverter responseConverter;
    private final CustomerRepository customerRepository;
    private final ChuckNorrisClient chuckNorrisClient;

    public Mono<ResponseDTO> generateResponse() {

        final var orderMono = this.orderRepository.findById(1L);
        final var memberMono = this.memberRepository.findById(1L);
        final var customerMono = this.customerRepository.findById(1L);
        final var chuckNorrisJokeMono = this.chuckNorrisClient.retrieveRandomChuckJoke();

        final var monoZipped = Mono.zip(orderMono, memberMono, customerMono, chuckNorrisJokeMono).flatMap(data -> {
            final var order = data.getT1();
            final var member = data.getT2();
            final var customer = data.getT3();
            final var chuckNorrisJoke = data.getT4();
            return this.responseConverter.toResponseDTO(customer, member, order, chuckNorrisJoke);
        });

        return monoZipped;

    }

}
