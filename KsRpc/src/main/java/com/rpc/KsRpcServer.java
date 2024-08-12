package com.rpc;

import com.rpc.config.Config;
import com.rpc.protocol.HttpServer;


public class KsRpcServer {
    public static void main(String[] args)  {
        int port = Config.getServerPort();
        HttpServer httpServer = new HttpServer();
        httpServer.start("localhost", port);
        System.out.println("Server started on port " + port);
    }
}
