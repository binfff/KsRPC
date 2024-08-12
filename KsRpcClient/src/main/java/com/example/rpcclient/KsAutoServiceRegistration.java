package com.example.rpcclient;

import org.springframework.cloud.client.serviceregistry.AbstractAutoServiceRegistration;
import org.springframework.cloud.client.serviceregistry.AutoServiceRegistrationProperties;
import org.springframework.cloud.client.serviceregistry.Registration;
import org.springframework.cloud.client.serviceregistry.ServiceRegistry;

/**
 * KsAutoServiceRegistration
 *
 * @author gjh
 * @version 1.0
 * @Date 2024-08-11 10:14
 * @description TODO
 */
public class KsAutoServiceRegistration extends AbstractAutoServiceRegistration<Registration> {



    public KsAutoServiceRegistration(ServiceRegistry<Registration> serviceRegistry,
        AutoServiceRegistrationProperties autoServiceRegistrationProperties) {
        super(serviceRegistry, autoServiceRegistrationProperties);
    }

    @Override
    protected Object getConfiguration() {
        return null;
    }

    @Override
    protected boolean isEnabled() {
        return true;
    }

    @Override
    protected Registration getRegistration() {
        return null;
    }

    @Override
    protected Registration getManagementRegistration() {
        return null;
    }

    @Override
    protected void register() {
        super.register();
    }
}
