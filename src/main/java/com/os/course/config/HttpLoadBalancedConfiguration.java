package com.os.course.config;

import com.os.course.util.ResourceType;
import org.springframework.cloud.client.DefaultServiceInstance;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.ReactiveDiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;

import java.util.Arrays;
import java.util.stream.Collectors;

@Configuration
public class HttpLoadBalancedConfiguration {

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
               return Arrays.stream(ResourceType.values())
                        .filter(resourceType -> resourceType.getResourceId().equalsIgnoreCase(serviceId))
                        .findAny()
                        .map(ResourceType::getPort)
                        .orElseThrow(() -> new RuntimeException("microservice with this id not found"));
            }

            @Override
            public Flux<String> getServices() {
                return Flux.fromIterable(Arrays.stream(ResourceType.values())
                        .map(ResourceType::getResourceId)
                        .collect(Collectors.toList()));
            }
        };
    }
}
