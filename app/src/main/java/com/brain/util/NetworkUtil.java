package com.brain.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * @Author FLE
 * @Company Brain Inc.
 * @Email hfredi35@gmail.com
 */
public class NetworkUtil {
    public static boolean hasNetwork(Context context) {
        boolean isConnected = false;
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnectedOrConnecting()) isConnected = true;
        return isConnected;
    }
}
