package com.example.rpcclient;

import com.alibaba.fastjson.JSON;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.net.HttpURLConnection;
import java.net.URL;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.EnvironmentAware;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

public class KsClientProxy implements InvocationHandler, EnvironmentAware {

    private Environment environment;
    // 服务端调用地址
    @Value("${ksrpc.registry.url}")
    private String registryUrl;

    @Value("${spring.application.name}")
    private String serviceName;

    @Value("${server.port}")
    private int serverPort;
    //    private final Map<String, String> serviceRegistry;  // 存储服务名到地址的映射
    private final Class<?> interfaceClass;

    public KsClientProxy(Class<?> interfaceClass) {
        this.interfaceClass = interfaceClass;

    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        KsClient classAnnotation = interfaceClass.getAnnotation(KsClient.class);
        if (classAnnotation == null) {
            throw new IllegalArgumentException("Interface must be annotated with @MyFeignClient");
        }

        String serviceName = classAnnotation.serviceName();
        String ip = KsRpcClient.getLocalHostLANAddress();
        String serviceUrl1 = "http://" + ip + ":" + serverPort;
        KsRpcClient client = KsRpcClient.getInstance(registryUrl, serviceName, serviceUrl1);
        String serviceUrl = client.discoverService(serviceName);
        if (serviceUrl == null) {
            throw new RuntimeException("Service not found: " + serviceName);
        }

        String path = getPathFromAnnotation(method);
        String url = serviceUrl + path;
        System.out.println("url====>" + url);

        HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
        connection.setRequestMethod(getRequestMethod(method));
        connection.setRequestProperty("Accept", "application/json");

        InputStream response = connection.getInputStream();
        BufferedReader reader = new BufferedReader(new InputStreamReader(response));
        StringBuilder responseBuilder = new StringBuilder();
        String line;

        while ((line = reader.readLine()) != null) {
            responseBuilder.append(line);
        }

        reader.close();
        // 获取方法的返回值类型
        Class<?> returnType = method.getReturnType();
        // 使用JSON库将响应转换为指定类型的对象
        return JSON.parseObject(responseBuilder.toString(), returnType);

    }

    public static <T> T createProxy(Class<T> interfaceClass) {
        KsClientProxy handler = new KsClientProxy(interfaceClass);
        return (T) Proxy.newProxyInstance(
            interfaceClass.getClassLoader(),
            new Class<?>[]{interfaceClass},
            handler
        );
    }

    @Override
    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }

    private static String getRequestMethod(Method method) {
        if (method.isAnnotationPresent(GetMapping.class)) {
            return "GET";
        } else if (method.isAnnotationPresent(PostMapping.class)) {
            return "POST";
        } else if (method.isAnnotationPresent(PutMapping.class)) {
            return "PUT";
        } else if (method.isAnnotationPresent(DeleteMapping.class)) {
            return "DELETE";
        } else if (method.isAnnotationPresent(PatchMapping.class)) {
            return "PATCH";
        } else if (method.isAnnotationPresent(RequestMapping.class)) {
            RequestMapping requestMapping = method.getAnnotation(RequestMapping.class);
            RequestMethod[] methods = requestMapping.method();
            if (methods.length == 0) {
                // 默认情况下，RequestMapping 支持所有 HTTP 方法，选择一个默认方法，例如 GET
                return "GET";
            } else {
                // 选择第一个指定的方法
                return methods[0].name();
            }
        } else {
            throw new UnsupportedOperationException("Unsupported request method");
        }
    }
    private static String getPathFromAnnotation(Method method) {
        if (method.isAnnotationPresent(GetMapping.class)) {
            return method.getAnnotation(GetMapping.class).value()[0];
        } else if (method.isAnnotationPresent(PostMapping.class)) {
            return method.getAnnotation(PostMapping.class).value()[0];
        } else if (method.isAnnotationPresent(PutMapping.class)) {
            return method.getAnnotation(PutMapping.class).value()[0];
        } else if (method.isAnnotationPresent(DeleteMapping.class)) {
            return method.getAnnotation(DeleteMapping.class).value()[0];
        } else if (method.isAnnotationPresent(PatchMapping.class)) {
            return method.getAnnotation(PatchMapping.class).value()[0];
        } else if (method.isAnnotationPresent(RequestMapping.class)) {
            return method.getAnnotation(RequestMapping.class).value()[0];
        } else {
            throw new UnsupportedOperationException("No mapping annotation found on method");
        }
    }
}
