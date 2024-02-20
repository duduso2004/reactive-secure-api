package br.com.duduso.reactive.secure.api.client;

import br.com.duduso.reactive.secure.api.dto.PostResponseDTO;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.service.annotation.GetExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface PostClient {

    @GetExchange("/posts")
    Flux<PostResponseDTO> findAll();

    @GetExchange("/posts/{id}")
    Mono<PostResponseDTO> findById(@PathVariable Long id);

}
