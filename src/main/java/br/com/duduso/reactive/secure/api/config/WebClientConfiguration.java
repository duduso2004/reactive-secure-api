package br.com.duduso.reactive.secure.api.config;

import br.com.duduso.reactive.secure.api.client.ChuckNorrisClient;
import br.com.duduso.reactive.secure.api.client.PostClient;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.config.WebFluxConfigurer;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.support.WebClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;
import reactor.netty.resources.ConnectionProvider;

import static java.util.UUID.randomUUID;

@Configuration
public class WebClientConfiguration implements WebFluxConfigurer {

    @Bean
    @SneakyThrows
    PostClient postClient(WebClient.Builder builder, @Value("${jsonplaceholder.api.url}") String baseUrl) {
        return HttpServiceProxyFactory.builder()
                .exchangeAdapter(WebClientAdapter.create(builder.baseUrl(baseUrl).build()))
                .build()
                .createClient(PostClient.class);
    }

    @Bean
    ChuckNorrisClient chuckNorrisClient(WebClient.Builder builder, @Value("${chucknorris.api.url}") String baseUrl) {
        return HttpServiceProxyFactory.builder()
                .exchangeAdapter(WebClientAdapter.create(builder.baseUrl(baseUrl).build()))
                .build()
                .createClient(ChuckNorrisClient.class);
    }

    public ConnectionProvider connectionProvider() {
        return ConnectionProvider.builder(randomUUID().toString())
                .maxConnections(5000)
                .pendingAcquireMaxCount(5000)
                .build();
    }

}
