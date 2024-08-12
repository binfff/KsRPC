package com.example.rpcclient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.URL;
import java.util.Timer;
import java.util.TimerTask;
import java.util.stream.Collectors;
import org.springframework.context.EnvironmentAware;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;



public class KsRpcClient {
    private static volatile KsRpcClient instance;

    private String registryUrl;

    private String serviceName;

    private String serviceUrl;

    private final Timer timer = new Timer();

    private KsRpcClient(String registryUrl,String serviceName,String serviceUrl) {
       this.registryUrl = registryUrl;
       this.serviceName = serviceName;
       this.serviceUrl = serviceUrl;
    }

    public static KsRpcClient getInstance(String registryUrl,String serviceName,String serviceUrl) {
        if (instance == null) {
            synchronized (KsRpcClient.class) {
                if (instance == null) {
                    instance = new KsRpcClient(registryUrl,serviceName,serviceUrl);
                }
            }
        }
        return instance;
    }

    public void registerService() throws Exception {
        String urlString = registryUrl + "/register?serviceName=" + serviceName + "&serviceUrl=" + serviceUrl;
        URL url = new URL(urlString);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("POST");
        conn.setDoOutput(true);

        try (OutputStream os = conn.getOutputStream()) {
            os.write(new byte[0]); // Empty body
        }

        int responseCode = conn.getResponseCode();
        if (responseCode == 200) {
            System.out.println("Service registered successfully.");
        } else {
            System.out.println("Failed to register service. Response code: " + responseCode);
        }
    }

    public String discoverService(String serviceName) throws Exception {
        String urlString = registryUrl + "/discover?serviceName=" + serviceName;
        URL url = new URL(urlString);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");

        int responseCode = conn.getResponseCode();
        if (responseCode == HttpURLConnection.HTTP_OK) { // 200
            try (BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()))) {
                return br.lines().collect(Collectors.joining("\n"));
            }
        } else if (responseCode == HttpURLConnection.HTTP_UNAVAILABLE) { // 503
            return null;
        } else {
            return null;
        }
    }


    public static String getLocalHostLANAddress() throws Exception {
        InetAddress inetAddress = InetAddress.getLocalHost();
        return inetAddress.getHostAddress();
    }

    public void startHeartbeat() {
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                sendHeartbeat();
            }
        }, 0, 5000); // 每5秒发送一次心跳
    }
    private void sendHeartbeat() {
        try {
            URL url = new URL(registryUrl + "/heartbeat?serviceName=" + serviceName+ "&serviceUrl=" + serviceUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            int responseCode = connection.getResponseCode();
            if (responseCode == 200) {
                System.out.println("Heartbeat sent successfully.");
            } else {
                System.out.println("Failed to send heartbeat.");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
