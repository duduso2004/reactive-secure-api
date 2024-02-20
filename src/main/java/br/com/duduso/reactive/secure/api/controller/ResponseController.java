package br.com.duduso.reactive.secure.api.controller;

import br.com.duduso.reactive.secure.api.service.ResponseService;
import br.com.duduso.reactive.secure.api.dto.ResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/protected")
@RequiredArgsConstructor
public class ResponseController {

    private final ResponseService service;

    @GetMapping("/response")
    public Mono<ResponseDTO> generateResponseDTO() {
        return this.service.generateResponse();
    }

}
