package com.example.rpcclient;

import org.springframework.beans.factory.FactoryBean;

public class KsClientFactoryBean<T> implements FactoryBean<T> {

    private final Class<T> type;

    public KsClientFactoryBean(Class<T> type) {
        this.type = type;

    }

    @Override
    public T getObject() throws Exception {
        return KsClientProxy.createProxy(type);
    }

    @Override
    public Class<?> getObjectType() {
        return this.type;
    }

    @Override
    public boolean isSingleton() {
        return true;
    }
}
