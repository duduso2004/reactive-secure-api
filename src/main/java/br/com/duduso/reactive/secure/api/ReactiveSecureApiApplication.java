package br.com.duduso.reactive.secure.api;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.reactive.ReactiveUserDetailsServiceAutoConfiguration;

import static org.springframework.boot.SpringApplication.run;

@SpringBootApplication(exclude = {ReactiveUserDetailsServiceAutoConfiguration.class})
public class ReactiveSecureApiApplication {

	public static void main(String[] args) {
		run(ReactiveSecureApiApplication.class, args);
	}

}
