package br.com.duduso.reactive.secure.api.exception;

public class JwtTokenTypeInvalidException extends JwtTokenValidationException {

    public JwtTokenTypeInvalidException() {
        super("O token JWT informado não é o esperado. Necessário informar um access_token.");
    }

}
