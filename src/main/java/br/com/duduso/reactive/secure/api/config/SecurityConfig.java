package br.com.duduso.reactive.secure.api.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsConfigurationSource;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;

import java.util.List;

import static org.springframework.security.config.web.server.SecurityWebFiltersOrder.AUTHENTICATION;

@Configuration
@EnableWebFluxSecurity
@EnableReactiveMethodSecurity
public class SecurityConfig {

    public static final String SPRING_SECURITY_CONTROL_METHOD = "spring.security.control.method";

    @Bean
    @ConditionalOnProperty(name = SPRING_SECURITY_CONTROL_METHOD, havingValue = "disabled")
    SecurityWebFilterChain springSecurityFilterChainDisabled(ServerHttpSecurity http, CorsConfigurationSource corsConfigurationSource) {
        return http.csrf(ServerHttpSecurity.CsrfSpec::disable)
                .cors(corsSpec -> corsSpec.configurationSource(corsConfigurationSource))
                .httpBasic(ServerHttpSecurity.HttpBasicSpec::disable)
                .authorizeExchange(exchanges -> exchanges.anyExchange().permitAll())
                .build();
    }

    @Bean
    @ConditionalOnProperty(name = SPRING_SECURITY_CONTROL_METHOD, havingValue = "custom")
    SecurityWebFilterChain springSecurityFilterChainCustom(ServerHttpSecurity http, CorsConfigurationSource corsConfigurationSource, SecurityFilter securityFilter) {
        return http.csrf(ServerHttpSecurity.CsrfSpec::disable)
                .cors(corsSpec -> corsSpec.configurationSource(corsConfigurationSource))
                .httpBasic(ServerHttpSecurity.HttpBasicSpec::disable)
                .authorizeExchange(exchanges -> exchanges
                        .pathMatchers("/protected/**").authenticated()
                        .anyExchange().permitAll()
                )
                .addFilterBefore(securityFilter, AUTHENTICATION)
                .build();
    }

    @Bean
    @ConditionalOnProperty(name = SPRING_SECURITY_CONTROL_METHOD, havingValue = "resource-server", matchIfMissing = true)
    SecurityWebFilterChain springSecurityFilterChainResourceServer(ServerHttpSecurity http, CorsConfigurationSource corsConfigurationSource, @Value("${spring.security.oauth2.resourceserver.jwt.jwk-set-uri}") String jwkUri) {
        return http.csrf(ServerHttpSecurity.CsrfSpec::disable)
                .cors(corsSpec -> corsSpec.configurationSource(corsConfigurationSource))
                .httpBasic(ServerHttpSecurity.HttpBasicSpec::disable)
                .authorizeExchange(exchanges -> exchanges
                        .pathMatchers("/protected/**").authenticated()
                        .anyExchange().permitAll()
                )
                .oauth2ResourceServer(oAuth2ResourceServerSpec -> oAuth2ResourceServerSpec
                        .jwt(jwtSpec -> jwtSpec.jwkSetUri(jwkUri)))
                .build();
    }

    @Bean
    CorsConfigurationSource corsConfigurationSource(@Value("${cors.linkorigem:*}") String linkOrigem) {
        final var configuration = new CorsConfiguration();
        configuration.setAllowedMethods(List.of("*"));
        configuration.setAllowedHeaders(List.of("*"));
        configuration.setAllowedOriginPatterns(List.of(linkOrigem));
        configuration.setAllowCredentials(true);
        final var source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

}


