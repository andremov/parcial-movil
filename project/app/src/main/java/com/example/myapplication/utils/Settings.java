package com.example.myapplication.utils;

import com.example.myapplication.activities.MainActivity;

public abstract class Settings {

    static String url = "http://192.168.1.71:8080/MovilAPI/api/";

    public static void linkServer(String ip, String port) {
        Settings.url = "http://"+ip+":"+port+"/MovilAPI/api/";
    }

    public static String getUrl() {
        return url;
    }

}
