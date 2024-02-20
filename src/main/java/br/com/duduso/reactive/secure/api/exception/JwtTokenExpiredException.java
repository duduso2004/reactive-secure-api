package br.com.duduso.reactive.secure.api.exception;

import io.jsonwebtoken.Claims;

import java.text.SimpleDateFormat;
import java.util.Date;

import static java.lang.String.format;

public class JwtTokenExpiredException extends JwtTokenValidationException {

    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

    public JwtTokenExpiredException(Claims claims) {
        super(format("Token JWT expirou em %s. Data/Hora de Geração do Token: %s. Data/Hora Atual: %s.",
                DATE_FORMAT.format(claims.getExpiration()),
                DATE_FORMAT.format(claims.getIssuedAt()),
                DATE_FORMAT.format(new Date())));
    }

}
