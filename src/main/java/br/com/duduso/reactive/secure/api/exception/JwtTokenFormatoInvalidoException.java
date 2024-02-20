package br.com.duduso.reactive.secure.api.exception;

public class JwtTokenFormatoInvalidoException extends JwtTokenValidationException {

    public JwtTokenFormatoInvalidoException() {
        super("O token informado não é reconhecido como um token JWT válido.");
    }

}
