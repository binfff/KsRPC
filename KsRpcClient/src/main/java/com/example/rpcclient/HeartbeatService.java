package com.example.rpcclient;

import jakarta.annotation.PostConstruct;
import java.util.Timer;
import java.util.TimerTask;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

@Service
public class HeartbeatService {
    private final Timer timer = new Timer();

    @Value("${spring.application.name}")
    private String serviceName;

    @Value("${server.port}")
    private String port;

    @Value("${ksrpc.registry.url}")
    private String serverAddress;

    public HeartbeatService() {
        // 无需任何操作
    }
    @PostConstruct
    public void startHeartbeat() {
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                sendHeartbeat();
            }
        }, 0, 5000); // 每5秒发送一次心跳
    }
    public void sendHeartbeat() {
        try {
            String  ip = KsRpcClient.getLocalHostLANAddress();
            String serviceUrl1 = "http://" + ip + ":" + port;
            URL url = new URL(serverAddress + "/heartbeat?serviceName=" + serviceName + "&serviceUrl=" + serviceUrl1);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            int responseCode = connection.getResponseCode();
            if (responseCode == 200) {
                System.out.println("Heartbeat sent successfully.");
            } else {
                System.out.println("Failed to send heartbeat.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
