package br.com.duduso.reactive.secure.api.client;

import br.com.duduso.reactive.secure.api.dto.ChuckNorrisResponseDTO;
import org.springframework.web.service.annotation.GetExchange;
import reactor.core.publisher.Mono;

public interface ChuckNorrisClient {

    @GetExchange("/jokes/random")
    Mono<ChuckNorrisResponseDTO> retrieveRandomChuckJoke();

}
