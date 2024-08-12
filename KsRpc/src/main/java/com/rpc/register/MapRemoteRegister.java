package com.rpc.register;

import com.rpc.common.ServiceInfo;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;

/**
 * MapRemoteRegister
 *
 * @author gjh
 * @version 1.0
 * @Date 2023-07-22 13:42
 * @description TODO
 */
@Slf4j
public class MapRemoteRegister {

    private static Map<String, List<ServiceInfo>> map = new HashMap<>();

    public static void regist(String serviceName, URL url) {
        map.computeIfAbsent(serviceName, k -> new ArrayList<>())
            .stream()
            .map(ServiceInfo::getUrl)
            .noneMatch(existingUrl -> existingUrl.equals(url));

        List<ServiceInfo> serviceList = map.get(serviceName);
        if (serviceList.stream().noneMatch(serviceInfo -> serviceInfo.getUrl().equals(url))) {
            serviceList.add(new ServiceInfo(url, System.currentTimeMillis()));
        }
    }

    public static List<ServiceInfo> get(String serviceName) {
        return map.get(serviceName);
    }

    public static Map<String, List<ServiceInfo>> getAllService(){
        return map;
    }

    public static void checkHealth(){
        log.debug("心跳检测开始");
        long currentTime = System.currentTimeMillis();
        Iterator<Map.Entry<String, List<ServiceInfo>>> iterator = map.entrySet().iterator();

        while (iterator.hasNext()) {
            Map.Entry<String, List<ServiceInfo>> entry = iterator.next();
            List<ServiceInfo> serviceInfos = entry.getValue();
            Iterator<ServiceInfo> serviceInfoIterator = serviceInfos.iterator();

            while (serviceInfoIterator.hasNext()) {
                ServiceInfo serviceInfo = serviceInfoIterator.next();
                if (currentTime - serviceInfo.getLastHeartbeatTime() > 10000) { // 10秒超时
                    log.info("Service instance " + serviceInfo.getUrl() + " is down.");
                    serviceInfoIterator.remove();
                }
            }

            if (serviceInfos.isEmpty()) {
                log.info("All instances of service " + entry.getKey() + " are down.");
                iterator.remove();
            }
        }
    }

    public static void receiveHeartbeat(String serviceName, URL url) {
        List<ServiceInfo> serviceInfos = map.get(serviceName);
        if (serviceInfos != null) {
            for (ServiceInfo serviceInfo : serviceInfos) {
                if (serviceInfo.getUrl().equals(url)) {
                    serviceInfo.setLastHeartbeatTime(System.currentTimeMillis());
                    System.out.println("Heartbeat received from " + serviceName + " at " + url);
                    return;
                }
            }
        }
    }
}
