package com.example.myapplication.utils;

public abstract class Settings {

    static String urlAPI = "http://192.168.1.71:8080/MovilAPI/api/";
    static String urlPush = "http://192.168.1.71:8080/MovilAPI/api/";

    public static void linkAPI(String ip, String port) {
        Settings.urlAPI = "http://"+ip+":"+port+"/MovilAPI/api/";
    }

    public static void linkPush(String ip, String port) {
        Settings.urlPush = "http://"+ip+":"+port+"/MovilAPI/api/";
    }

    public static String getUrlAPI() {
        return urlAPI;
    }

    public static String getUrlPush() {
        return urlPush;
    }

}
