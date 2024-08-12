package com.rpc.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class Config {
    private static Properties properties = new Properties();
    private static final String CONFIG_FILE = "application.properties";

    static {
        try (InputStream input = Config.class.getClassLoader().getResourceAsStream(CONFIG_FILE)) {
            if (input == null) {
                throw new IllegalArgumentException("Sorry, unable to find " + CONFIG_FILE);
            }
            properties.load(input);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public static int getServerPort() {
        return Integer.parseInt(properties.getProperty("server.port", "8088"));
    }
}
