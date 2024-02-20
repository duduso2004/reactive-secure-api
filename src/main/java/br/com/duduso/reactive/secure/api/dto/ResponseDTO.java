package br.com.duduso.reactive.secure.api.dto;

import br.com.duduso.reactive.secure.api.model.Customer;
import br.com.duduso.reactive.secure.api.model.Member;
import br.com.duduso.reactive.secure.api.model.Order;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(NON_NULL)
public class ResponseDTO {

    private Customer customer;
    private Member member;
    private Order order;
    private ChuckNorrisResponseDTO chuckNorrisJoke;
    @Builder.Default
    private List<PostResponseDTO> posts = new ArrayList<>();

}
