package com.restur.msgrtest.consts;

public class PathsToServer {
    //Main paths
    public static String getServerPath() {
        return "http://192.168.1.203:8080";
    }


    //User paths
    public static String getUserPath() {
        return "/user";
    }

    public static String getRegistrationPath() { return "/registration"; }

    public static String getLoginPath() { return "/login"; }

    public static String getCheckLoginPath() {return "/checkLogin";}

    public static String getChatList() {return "/getChatList";}


    //Chat paths
    public static String getChatPath() {
        return "/chat";
    }


}