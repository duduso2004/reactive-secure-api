package br.com.duduso.reactive.secure.api.exception;

public class JwtTokenValidationException extends RuntimeException {

    public JwtTokenValidationException() {
    }

    public JwtTokenValidationException(String message) {
        super(message);
    }

    public JwtTokenValidationException(String message, Throwable cause) {
        super(message, cause);
    }

}
