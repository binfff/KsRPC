package com.rpc.protocol;

import com.rpc.protocol.handler.AllServiceHandler;
import com.rpc.protocol.handler.DiscoverHandler;
import com.rpc.protocol.handler.HeartbeatHandler;
import com.rpc.protocol.handler.RegisterHandler;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * DispatcherServlet
 *
 * @author gjh
 * @version 1.0
 * @Date 2023-07-21 18:05
 * @description TODO
 */
public class DispatcherServlet extends HttpServlet {

  @Override
  protected void service(HttpServletRequest req, HttpServletResponse resp)
      throws ServletException, IOException {
    // 过滤器或者责任链的模式，用不同的handler处理不同的请求
    String path = req.getServletPath();
    String uri = req.getRequestURI();


    if ("/register".equals(uri)) {
      new RegisterHandler().handler(req,resp);
    } else if ("/discover".equals(uri)) {
      new DiscoverHandler().handler(req,resp);
    }else if ("/heartbeat".equals(uri)){
      new HeartbeatHandler().handler(req,resp);
    } else if ("/getAllService".equals(uri)){
      new AllServiceHandler().handler(req,resp);
    }else {
      resp.sendError(HttpServletResponse.SC_NOT_FOUND, "Invalid request path");
    }

//    new HttpServerHandler().handler(req,resp);
  }
}
