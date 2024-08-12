package com.rpc.protocol.handler;

import com.rpc.register.MapRemoteRegister;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URL;

/**
 * RegisterHandler
 *
 * @author gjh
 * @version 1.0
 * @Date 2023-07-22 15:39
 * @description TODO
 */
public class RegisterHandler {
    public void handler(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String serviceName = req.getParameter("serviceName");
        String serviceUrl = req.getParameter("serviceUrl");

        if (serviceName != null && !serviceName.isEmpty() && serviceUrl != null && !serviceUrl.isEmpty()) {
            try {
                URL url = new URL(serviceUrl);
                MapRemoteRegister.regist(serviceName,url);
                resp.getWriter().println("Service registered successfully.");
            } catch (Exception e) {
                resp.getWriter().println("Error registering service: " + e.getMessage());
            }
        } else {
            resp.getWriter().println("Invalid request.");
        }
    }
}
