package kr.co.direa.external.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.DefaultUriBuilderFactory;

import static kr.co.direa.common.constant.Constants.GO_API_BASE_URL;

@Configuration
public class WebClientConfig {

    @Bean
    public WebClient.Builder webClientBuilder() {
        return WebClient.builder();
    }

    @Bean
    public WebClient weatherWebClient() {
        DefaultUriBuilderFactory factory = new DefaultUriBuilderFactory(GO_API_BASE_URL);
        factory.setEncodingMode(DefaultUriBuilderFactory.EncodingMode.VALUES_ONLY);

        return WebClient.builder()
                .baseUrl(GO_API_BASE_URL)
                .uriBuilderFactory(factory)
                .build();
    }
}
