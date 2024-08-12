package com.rpc.proxy;

import com.rpc.common.Invocation;
import com.rpc.common.ServiceInfo;
import com.rpc.loadbalance.LoadBalance;
import com.rpc.protocol.HttpClient;
import com.rpc.register.MapRemoteRegister;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.net.URL;
import java.util.List;
import java.util.stream.Collectors;

/**
 * ProxyFactory
 *
 * @author gjh
 * @version 1.0
 * @Date 2023-07-22 10:52
 * @description TODO
 */
public class ProxyFactory {

    public static <T> T getProxy(Class interfaceClass) {
        Object proxyInstance = Proxy.newProxyInstance(interfaceClass.getClassLoader(),
            new Class[]{interfaceClass},
            new InvocationHandler() {
                @Override
                public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                    //new invocation对象，然后发送请求
                    Invocation invocation = new Invocation(interfaceClass.getName(),
                        method.getName(),
                        method.getParameterTypes(), args);
                    HttpClient client = new HttpClient();
                    // 服务发现
                    List<ServiceInfo> serviceInfos = MapRemoteRegister.get(interfaceClass.getName());
                    List<URL> urls = serviceInfos.stream().map(ServiceInfo::getUrl)
                        .collect(Collectors.toList());
                    // 负载均衡
                    URL url = LoadBalance.random(urls);
                    // 服务调用
                    String send = client.send(url.getHost(),url.getPort(),invocation);
                    return send;
                }
            });
        return (T) proxyInstance;
    }
}
