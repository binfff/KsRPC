package com.rpc.protocol;

import com.rpc.common.Invocation;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.ObjectInputStream;

/**
 * HttpServerHandler
 *
 * @author gjh
 * @version 1.0
 * @Date 2023-07-21 18:06
 * @description TODO
 */
public class HttpServerHandler {

    public void handler(HttpServletRequest req, HttpServletResponse resp){
        try {
            Invocation invocation =(Invocation) new ObjectInputStream(req.getInputStream()).readObject();
            String interfaceName = invocation.getInterfaceName();

        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
