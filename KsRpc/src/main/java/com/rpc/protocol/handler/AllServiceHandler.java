package com.rpc.protocol.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rpc.common.ServiceInfo;
import com.rpc.loadbalance.LoadBalance;
import com.rpc.register.MapRemoteRegister;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * AllServiceHandler
 *
 * @author gjh
 * @version 1.0
 * @Date 2024-07-30 19:19
 * @description TODO
 */
public class AllServiceHandler {

    private final ObjectMapper objectMapper = new ObjectMapper(); // 用于将数据转换为 JSON 格式


    public void handler(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        Map<String, List<ServiceInfo>> allService = MapRemoteRegister.getAllService();
        Map<String, List<URL>> map = new HashMap<>();
        allService.entrySet().stream().forEach(x -> map.put(x.getKey(), x.getValue().stream().map(ServiceInfo::getUrl).collect(
            Collectors.toList())));
        // 设置响应内容类型为 JSON
        resp.setContentType("application/json");

        // 将 map 转换为 JSON 字符串
        String jsonResponse = objectMapper.writeValueAsString(map);

        // 设置响应内容长度
        resp.setContentLength(jsonResponse.length());

        // 将 JSON 字符串写入响应
        resp.getWriter().write(jsonResponse);
    }
}
