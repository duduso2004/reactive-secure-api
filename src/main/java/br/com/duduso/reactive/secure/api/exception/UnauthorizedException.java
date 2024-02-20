package br.com.duduso.reactive.secure.api.exception;

public class UnauthorizedException extends RuntimeException {

    public UnauthorizedException() {
        super("É necessário autenticação para acessar esse recurso");
    }

}
