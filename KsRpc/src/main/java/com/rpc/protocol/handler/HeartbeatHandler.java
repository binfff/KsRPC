package com.rpc.protocol.handler;

import com.rpc.common.ServiceInfo;
import com.rpc.register.MapRemoteRegister;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import lombok.extern.slf4j.Slf4j;

/**
 * HearbeatHandler
 *
 * @author gjh
 * @version 1.0
 * @Date 2023-07-27 17:45
 * @description TODO
 */
@Slf4j
public class HeartbeatHandler {
    private static final Timer timer = new Timer();

    public void handler(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String serviceName = req.getParameter("serviceName");
        String serviceUrl = req.getParameter("serviceUrl");
        log.debug("收到心跳======>"+serviceName+":"+serviceUrl);
        if (serviceName != null && serviceUrl != null) {
            try {
                URL url = new URL(serviceUrl);
                MapRemoteRegister.receiveHeartbeat(serviceName, url);
                resp.setStatus(HttpServletResponse.SC_OK);
                resp.getWriter().write("Heartbeat received");
            } catch (Exception e) {
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                resp.getWriter().write("Invalid URL format");
            }
        } else {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().write("Missing serviceName or serviceUrl parameter");
        }
    }

    public static void startHealthCheck() {
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                MapRemoteRegister.checkHealth();
            }
        }, 0, 5000); // 每5秒检查一次
    }
}
