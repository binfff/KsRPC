package com.rpc;

import com.rpc.config.Config;
import com.rpc.protocol.HttpServer;
import org.apache.catalina.LifecycleException;

public class KsRpcServer {
    public static void main(String[] args) throws LifecycleException {
        int port = Config.getServerPort();
        HttpServer httpServer = new HttpServer();
        httpServer.start("localhost", port);
        System.out.println("Server started on port " + port);
    }
}
