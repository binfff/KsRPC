package com.example.rpcclient;

import com.example.rpcclient.HeartbeatService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
public class HeartbeatAutoConfiguration {

    @Bean
    public HeartbeatService heartbeatService() {
        return new HeartbeatService();
    }
}
