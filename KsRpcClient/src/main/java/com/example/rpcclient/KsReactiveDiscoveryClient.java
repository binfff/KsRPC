package com.example.rpcclient;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.DefaultServiceInstance;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.ReactiveDiscoveryClient;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class KsReactiveDiscoveryClient implements ReactiveDiscoveryClient {
    private static final Logger logger = LoggerFactory.getLogger(KsReactiveDiscoveryClient.class);

    private final WebClient webClient = WebClient.builder().build();
    @Value("${ksrpc.registry.url}")
    private String serverAddress;

    @Override
    public String description() {
        return "My custom reactive discovery client implementation with a map-based registry.";
    }

    @Override
    public Flux<ServiceInstance> getInstances(String serviceId) {
        logger.info("Fetching instances for serviceId: {}", serviceId);
        return fetchInstancesFromRegistry(serviceId)
            .flatMapMany(urls -> {
                List<ServiceInstance> instances = urls.stream()
                    .map(url -> new DefaultServiceInstance(
                        url.toString(),
                        serviceId,
                        url.getHost(),
                        url.getPort(),
                        "https".equalsIgnoreCase(url.getProtocol())
                    ))
                    .collect(Collectors.toList());
                logger.info("Instances fetched for serviceId {}: {}", serviceId, instances);
                return Flux.fromIterable(instances);
            });
    }

    @Override
    public Flux<String> getServices() {
        logger.info("Fetching all services");
        return fetchAllServicesFromRegistry()
            .flatMapMany(allServices -> {
                List<String> serviceNames = new ArrayList<>(allServices.keySet());
                logger.info("Services fetched: {}", serviceNames);
                return Flux.fromIterable(serviceNames);
            });
    }

    private Mono<List<URL>> fetchInstancesFromRegistry(String serviceId) {
        logger.info("Fetching instances from registry for serviceId: {}", serviceId);
        return fetchAllServicesFromRegistry()
            .map(allServices -> {
                List<URL> urls = allServices.getOrDefault(serviceId, new ArrayList<>());
                logger.info("Instances fetched from registry for serviceId {}: {}", serviceId, urls);
                return urls;
            });
    }

    private Mono<Map<String, List<URL>>> fetchAllServicesFromRegistry() {
        String urlString = serverAddress + "/getAllService";
        return webClient.get()
            .uri(urlString)
            .retrieve()
            .bodyToMono(new ParameterizedTypeReference<Map<String, List<URL>>>() {})
            .doOnNext(result -> {
                logger.info("Fetched URL result: {}", result);
            })
            .onErrorResume(e -> {
                logger.error("Failed to fetch services from registry: {}", e.getMessage(), e);
                return Mono.just(new HashMap<>());
            });
    }
}
