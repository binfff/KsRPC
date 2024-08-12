package com.example.rpcclient;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Configuration;

@Configuration
public class KsRpcAutoConfiguration {

    @Value("${ksrpc.registry.url}")
    private String registryUrl;

    @Value("${spring.application.name}")
    private String serviceName;

    @Value("${server.port}")
    private int serverPort;

//    @Override
//    public void onApplicationEvent(ApplicationReadyEvent event) {
//        try {
//            String ip = KsRpcClient.getLocalHostLANAddress();
//            String serviceUrl = "http://"+ip+":" + serverPort;
//            KsRpcClient client = KsRpcClient.getInstance(registryUrl,serviceName,serviceUrl);
//            client.registerService();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
}
