package com.rpc.protocol.handler;

import com.rpc.common.ServiceInfo;
import com.rpc.loadbalance.LoadBalance;
import com.rpc.register.MapRemoteRegister;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * DiscoverHandler
 *
 * @author gjh
 * @version 1.0
 * @Date 2023-07-22 15:36
 * @description TODO
 */
public class DiscoverHandler {

    public void handler(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String serviceName = req.getParameter("serviceName");

        resp.setContentType("text/plain");
        PrintWriter writer = resp.getWriter();

        if (serviceName != null && !serviceName.isEmpty()) {
            // 服务发现
            List<ServiceInfo> serviceInfos = MapRemoteRegister.get(serviceName);
            if (serviceInfos == null || serviceInfos.isEmpty()) {
                resp.setStatus(HttpServletResponse.SC_SERVICE_UNAVAILABLE); // 503
                writer.println("Service unavailable.");
            } else {
                List<URL> urls = serviceInfos.stream()
                    .map(ServiceInfo::getUrl)
                    .collect(Collectors.toList());
                // 负载均衡
                URL url = LoadBalance.random(urls);
                if (url == null) {
                    writer.println("");
                } else {
                    writer.println(url);
                }
            }
        } else {
            writer.println("Invalid request.");
        }

        writer.flush();
    }
}
