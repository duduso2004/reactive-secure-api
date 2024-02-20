package br.com.duduso.reactive.secure.api.config;

import br.com.duduso.reactive.secure.api.exception.JwtTokenAssinaturaInvalidaException;
import br.com.duduso.reactive.secure.api.exception.JwtTokenExpiredException;
import br.com.duduso.reactive.secure.api.exception.JwtTokenFormatoInvalidoException;
import br.com.duduso.reactive.secure.api.exception.JwtTokenTypeInvalidException;
import br.com.duduso.reactive.secure.api.exception.UnauthorizedException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Optional;

import static br.com.duduso.reactive.secure.api.config.SecurityConfig.SPRING_SECURITY_CONTROL_METHOD;
import static java.util.Base64.getDecoder;
import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;
import static java.util.Optional.ofNullable;
import static java.util.stream.Collectors.toCollection;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.security.core.context.ReactiveSecurityContextHolder.withAuthentication;
import static reactor.core.publisher.Mono.fromCallable;

@Slf4j
@Component
@ConditionalOnProperty(name = SPRING_SECURITY_CONTROL_METHOD, havingValue = "custom")
public class SecurityFilter implements WebFilter {

    private static final String RSA = "RSA";
    private static final String BEARER = "Bearer ";
    private static final String PROTECTED_PATH = "/protected";

    @Value("${keycloak.public.key}")
    private String publicKey;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        final var path = exchange.getRequest().getPath().toString();
        if (!path.startsWith(PROTECTED_PATH)) {
            return chain.filter(exchange);
        }
        final var authorizationToken = retrieveAuthorizatonTokenFromHeaders(exchange.getRequest().getHeaders());
        if (nonNull(authorizationToken)) {
            return fromCallable(() -> getAuthentication(authorizationToken, getClaimsFromToken(authorizationToken)))
                    .flatMap(authentication -> chain.filter(exchange).contextWrite(withAuthentication(authentication)));
        }
        return Mono.error(UnauthorizedException::new);
    }

    private String retrieveAuthorizatonTokenFromHeaders(HttpHeaders headers) {
        final var authorizatonToken = headers.getFirst(AUTHORIZATION);
        if (isNull(authorizatonToken) || !authorizatonToken.startsWith(BEARER)) {
            return null;
        }
        return authorizatonToken.substring(7).trim();
    }

    private Claims getClaimsFromToken(String token) {
        try {
            return Jwts.parser().setSigningKey(getParsedPublicKey().get()).parseClaimsJws(token).getBody();
        } catch (ExpiredJwtException ex) {
            throw new JwtTokenExpiredException(ex.getClaims());
        } catch (MalformedJwtException | UnsupportedJwtException | IllegalArgumentException ex) {
            throw new JwtTokenFormatoInvalidoException();
        } catch (SignatureException ex) {
            throw new JwtTokenAssinaturaInvalidaException();
        }
    }

    private Optional<RSAPublicKey> getParsedPublicKey() {
        try {
            final var keySpecX509 = new X509EncodedKeySpec(getDecoder().decode((this.publicKey)));
            final var rsaPublicKey = (RSAPublicKey) KeyFactory.getInstance(RSA).generatePublic(keySpecX509);
            return Optional.of(rsaPublicKey);
        } catch (NoSuchAlgorithmException | InvalidKeySpecException ex) {
            log.error("Erro ao tentar fazer o parse da chave publica fornecida pelo Keycloak", ex);
            return Optional.empty();
        }
    }

    private Authentication getAuthentication(String token, Claims claims) {
        try {
            if (!BEARER.trim().equals(claims.get("typ"))) {
                throw new JwtTokenTypeInvalidException();
            }
            final var username = (String) claims.get("preferred_username");
            final var rolesList = ofNullable((LinkedHashMap<?, ?>) claims.get("realm_access"))
                    .map(realmAccess -> (Collection<String>) realmAccess.get("roles"))
                    .orElse(new ArrayList<>());
            final var authorities = rolesList.stream().map(SimpleGrantedAuthority::new).collect(toCollection(ArrayList::new));
            final var principal = new User(username, "", authorities);
            return new PreAuthenticatedAuthenticationToken(principal, token, authorities);
        } catch (Exception ex) {
            log.error("Erro ao Tentar Construir Authentication a Partir do Token", ex);
            throw ex;
        }
    }

}
