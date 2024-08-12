package com.rpc.common;

import java.net.URL;

public class ServiceInfo {

    private final URL url;
    private long lastHeartbeatTime;

    public ServiceInfo(URL url, long lastHeartbeatTime) {
        this.url = url;
        this.lastHeartbeatTime = lastHeartbeatTime;
    }

    public URL getUrl() {
        return url;
    }

    public long getLastHeartbeatTime() {
        return lastHeartbeatTime;
    }

    public void setLastHeartbeatTime(long lastHeartbeatTime) {
        this.lastHeartbeatTime = lastHeartbeatTime;
    }
}