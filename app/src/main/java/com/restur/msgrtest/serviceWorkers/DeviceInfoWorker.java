package com.restur.msgrtest.serviceWorkers;

import android.content.Context;
import android.provider.Settings;
import android.provider.Settings.System;

import java.net.NetworkInterface;
import java.util.Collections;
import java.util.List;

public class DeviceInfoWorker {

    //copy-pasted code
    public static String getMacAddress() {
        try {
            List<NetworkInterface> all = Collections.list(NetworkInterface.getNetworkInterfaces());
            for (NetworkInterface nif : all) {
                if (!nif.getName().equalsIgnoreCase("wlan0")) continue;

                byte[] macBytes = nif.getHardwareAddress();
                if (macBytes == null) {
                    return "";
                }

                StringBuilder res1 = new StringBuilder();
                for (byte b : macBytes) {
                    String hex = Integer.toHexString(b & 0xFF);
                    if (hex.length() == 1)
                        hex = "0".concat(hex);
                    res1.append(hex.concat(":"));
                }

                if (res1.length() > 0) {
                    res1.deleteCharAt(res1.length() - 1);
                }
                return res1.toString();
            }
        } catch (Exception ex) {
        }
        return "";
    }

    public static String getImeiHashThisDevice(Context context) {
        return HasherWorker.encryptThisString(System.getString(context.getContentResolver(),
                Settings.Secure.ANDROID_ID));
    }
}
