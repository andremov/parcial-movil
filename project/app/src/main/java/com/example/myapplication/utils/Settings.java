package com.example.myapplication.utils;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Collections;
import java.util.List;

public abstract class Settings {

    static String urlAPI = "http://192.168.0.10:8080/MovilAPI/api/";
    static String urlPushIP = "192.168.1.71";
    static String urlPushPort = "9090";

    public static void linkAPI(String ip, String port) {
        Settings.urlAPI = "http://"+ip+":"+port+"/MovilAPI/api/";
    }

    public static void linkPush(String ip, String port) {
        Settings.urlPushIP = ip;
        Settings.urlPushPort = port;
    }

    public static String getPushIP() {
        return urlPushIP;
    }

    public static int getPushPort() {
        return Integer.parseInt(urlPushPort);
    }

    public static String getUrlAPI() {
        return urlAPI;
    }

    public static String getIPAddress(boolean useIPv4) {
        try {
            List<NetworkInterface> interfaces = Collections.list(NetworkInterface.getNetworkInterfaces());
            for (NetworkInterface intf : interfaces) {
                List<InetAddress> addrs = Collections.list(intf.getInetAddresses());
                for (InetAddress addr : addrs) {
                    if (!addr.isLoopbackAddress()) {
                        String sAddr = addr.getHostAddress();
                        //boolean isIPv4 = InetAddressUtils.isIPv4Address(sAddr);
                        boolean isIPv4 = sAddr.indexOf(':')<0;

                        if (useIPv4) {
                            if (isIPv4)
                                return sAddr;
                        } else {
                            if (!isIPv4) {
                                int delim = sAddr.indexOf('%'); // drop ip6 zone suffix
                                return delim<0 ? sAddr.toUpperCase() : sAddr.substring(0, delim).toUpperCase();
                            }
                        }
                    }
                }
            }
        } catch (Exception ignored) { } // for now eat exceptions

        return "";
    }
}
