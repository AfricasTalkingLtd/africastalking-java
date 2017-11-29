package com.africastalking.test;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public final class Fixtures {
    public static String API_KEY;
    public static String USERNAME;
    public static final boolean DEBUG = true;
    public static final long TIMEOUT = 3500;

    static {
        try {
            String filePath = "../../local.properties"; // relative to libs/core
            Properties properties = new Properties();
            FileInputStream is = new FileInputStream(filePath);
            properties.load(is);
            API_KEY = properties.getProperty("api.key");
            USERNAME = properties.getProperty("api.username");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
