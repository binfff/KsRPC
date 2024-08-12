package com.example.rpcclient;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.serviceregistry.Registration;
import org.springframework.cloud.client.serviceregistry.ServiceRegistry;
import org.springframework.stereotype.Component;

/**
 * KsServiceRegistry
 *
 * @author gjh
 * @version 1.0
 * @Date 2024-08-10 13:59
 * @description TODO
 */

public class KsServiceRegistry implements ServiceRegistry<Registration> {
    @Value("${ksrpc.registry.url}")
    private String registryUrl;

    @Value("${spring.application.name}")
    private String serviceName;

    @Value("${server.port}")
    private int serverPort;
    @Override
    public void register(Registration registration) {
        try {
            String ip = KsRpcClient.getLocalHostLANAddress();
            String serviceUrl = "http://"+ip+":" + serverPort;
            KsRpcClient client = KsRpcClient.getInstance(registryUrl,serviceName,serviceUrl);
            client.registerService();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void deregister(Registration registration) {

    }

    @Override
    public void close() {

    }

    @Override
    public void setStatus(Registration registration, String status) {

    }

    @Override
    public <T> T getStatus(Registration registration) {
        return null;
    }
}
