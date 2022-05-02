package com.restur.msgrtest.connection;

import com.restur.msgrtest.consts.PathsToServer;

public class CreateUrlRequest {

    //User URLs
    public static String createRegistrationRequest() {
        return PathsToServer.getServerPath() + PathsToServer.getUserPath()
                + PathsToServer.getRegistrationPath();
    }

    public static String createLoginRequest(String userTag, String passHash, String deviceIdHash) {
        return PathsToServer.getServerPath() +
                PathsToServer.getUserPath() + PathsToServer.getLoginPath() +
                "?userTag=" + userTag + "&pass=" + passHash + "&deviceIdHash=" +
                deviceIdHash;
    }

    public static String createCheckLogInStatus(Long userId, String deviceIdHash) {
        return PathsToServer.getServerPath() + PathsToServer.getUserPath()
                + PathsToServer.getCheckLoginPath();
    }


    public static String createLoadAllChatsRequest(Long userId, String idDeviceHash) {
        return PathsToServer.getServerPath() + PathsToServer.getUserPath() +
                PathsToServer.getChatList() + "?userId=" + userId + "&idDeviceHash=" + idDeviceHash;
    }

    //Chat URLs

}
