package com.example.rpcclient;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.DefaultServiceInstance;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;

/**
 * MyCustomDiscoveryClient
 *
 * @author gjh
 * @version 1.0
 * @Date 2024-07-30 15:29
 * @description TODO
 */

public class KsCustomDiscoveryClient implements DiscoveryClient {
    @Value("${ksrpc.registry.url}")
    private String serverAddress;


    private final ObjectMapper objectMapper = new ObjectMapper();
    @Override
    public String description() {
        return "My custom discovery client implementation with a map-based registry.";
    }

    @Override
    public List<ServiceInstance> getInstances(String serviceId) {
        // 从注册中心获取服务实例列表
        List<URL> urls = fetchInstancesFromRegistry(serviceId);
        if (urls == null) {
            return new ArrayList<>(); // 如果没有找到，返回空列表
        }

        // 转换 URL 为 Spring Cloud 的 ServiceInstance 对象
        return urls.stream()
            .map(url -> new DefaultServiceInstance(
                url.toString(),  // ID 可以使用 URL 的字符串表示
                serviceId,
                url.getHost(),
                url.getPort(),
                "https".equalsIgnoreCase(url.getProtocol()) // 如果 URL 协议是 https，标记为 secure
            ))
            .collect(Collectors.toList());
    }

    @Override
    public List<String> getServices() {
        // 从注册中心获取所有服务信息
        Map<String, List<URL>> allServices = fetchAllServicesFromRegistry();
        return new ArrayList<>(allServices.keySet());
    }

    // 从注册中心获取服务实例列表
    private List<URL> fetchInstancesFromRegistry(String serviceId) {
        // 调用注册中心的接口获取数据
        Map<String, List<URL>> allServices = fetchAllServicesFromRegistry();
        System.out.println("所有服务列表==========>"+allServices);
        return allServices.getOrDefault(serviceId, new ArrayList<>());
    }

    // 从注册中心获取所有服务信息
    private Map<String, List<URL>> fetchAllServicesFromRegistry() {

        // 处理响应
        try  {
            // 构造完整的 URL
            String urlString = serverAddress + "/getAllService";
            URL url = new URL(urlString);

            // 创建 HTTP 连接
            HttpURLConnection  connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Accept", "application/json");
            // 处理响应
            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuilder responseBuilder = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                responseBuilder.append(line);
            }
            reader.close();

            String response = responseBuilder.toString();

            // 解析 JSON 字符串为 Map<String, List<URL>>
            return objectMapper.readValue(response, new TypeReference<Map<String, List<URL>>>() {});
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new HashMap<>();
    }
}
