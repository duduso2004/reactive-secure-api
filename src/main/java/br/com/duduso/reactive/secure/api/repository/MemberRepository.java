package br.com.duduso.reactive.secure.api.repository;

import br.com.duduso.reactive.secure.api.model.Member;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

public interface MemberRepository extends ReactiveCrudRepository<Member, Long> {

}
