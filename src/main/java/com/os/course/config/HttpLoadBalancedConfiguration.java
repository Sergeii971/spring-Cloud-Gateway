package com.os.course.config;

import org.springframework.cloud.client.DefaultServiceInstance;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.ReactiveDiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;

@Configuration
public class HttpLoadBalancedConfiguration {
    private static final String SONG_SERVICE_ID = "SONG-SERVICE";
    private static final String RESOURCE_SERVICE_ID = "RESOURCE-SERVICE";

    @LoadBalanced
    @Bean
    WebClient.Builder webClientBuilder() {
        return WebClient.builder();
    }

    @Bean
    public ReactiveDiscoveryClient customDiscoveryClient() {
        return new ReactiveDiscoveryClient() {
            @Override
            public String description() {
                return "Calling API";
            }

            @Override
            public Flux<ServiceInstance> getInstances(String serviceId) {
                return Flux.just(getServicePort(serviceId))
                        .map(port -> new DefaultServiceInstance(serviceId + "-" + port, serviceId,
                                "localhost", port, false));
            }

            private int getServicePort(String serviceId) {
                return RESOURCE_SERVICE_ID.equalsIgnoreCase(serviceId) ? 8081 : 8082;
            }

            @Override
            public Flux<String> getServices() {
                return Flux.just(RESOURCE_SERVICE_ID, SONG_SERVICE_ID);
            }
        };
    }
}
