package com.example.rpcclient;

import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cloud.client.CommonsClientAutoConfiguration;
import org.springframework.cloud.client.discovery.simple.SimpleDiscoveryClientAutoConfiguration;
import org.springframework.cloud.client.serviceregistry.AutoServiceRegistrationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnProperty(name = "spring.cloud.kscustom.discovery.enabled", matchIfMissing = true)
@AutoConfigureBefore({SimpleDiscoveryClientAutoConfiguration.class, CommonsClientAutoConfiguration.class})
public class KsCustomDiscoveryClientAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public KsCustomDiscoveryClient ksCustomDiscoveryClient() {
        return new KsCustomDiscoveryClient();
    }
    @Bean
    @ConditionalOnMissingBean
    public KsServiceRegistry ksServiceRegistry() {
        return new KsServiceRegistry();
    }

    @Bean
    @ConditionalOnBean(AutoServiceRegistrationProperties.class)
    public KsAutoServiceRegistration ksAutoServiceRegistration(
        KsServiceRegistry registry,
        AutoServiceRegistrationProperties autoServiceRegistrationProperties) {
        return new KsAutoServiceRegistration(registry,
            autoServiceRegistrationProperties);
    }

    @Bean
    @ConditionalOnMissingBean
    public KsReactiveDiscoveryClient ksReactiveDiscoveryClient() {
        return new KsReactiveDiscoveryClient();
    }
}
