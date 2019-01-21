package io.jcervelin.ideas.geofinder.recommendations.configs.feign;

import feign.Request;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FeignConfig {

    @Value("${foursquare.connectTimeout}")
    private Integer connectTimeout;

    @Value("${foursquare.readTimeout}")
    private Integer readTimeout;

    @Bean
    public SpringWebClientErrorDecoder errorDecoder() {
        return new SpringWebClientErrorDecoder();
    }

    @Bean
    public Request.Options options() {
        return new Request.Options(connectTimeout, readTimeout);
    }

}