package com.africastalking.utils;


import java.io.IOException;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Enumeration;

public class Networking {


    public static InetAddress getDefaultInetAddress() throws IOException {
        final Enumeration<NetworkInterface> ifaces = NetworkInterface.getNetworkInterfaces();

        while (ifaces.hasMoreElements()) {
            NetworkInterface iface = ifaces.nextElement();
            Enumeration<InetAddress> inAddrs = iface.getInetAddresses();
            while (inAddrs.hasMoreElements()) {
                InetAddress inAddr = inAddrs.nextElement();
                if (inAddr.isLoopbackAddress() || !inAddr.isReachable(100)){
                    continue;
                }
                return inAddr;
            }
        }
        return null;
    }
}
