package br.com.duduso.reactive.secure.api.exception;

public class JwtTokenAssinaturaInvalidaException extends JwtTokenValidationException {

    public JwtTokenAssinaturaInvalidaException() {
        super("A assinatura JWT não corresponde a assinatura calculada localmente. A validade do JWT não pode ser afirmada e não deve ser confiável.");
    }

}
