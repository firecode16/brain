package com.brain.util;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.ConnectivityManager;
import android.os.Build;

import androidx.annotation.RequiresApi;

import com.brain.R;

import java.util.ArrayList;
import java.util.Base64;
import java.util.concurrent.TimeoutException;

public class Util {

    private final Context context;

    public static int[] getTabIcon = {
            R.drawable.ic_home_public,
            R.drawable.ic_chat,
            R.drawable.ic_profile
    };

    public Util(Context context) {
        this.context = context;
    }

    public static <E> boolean containsInstance(ArrayList<E> list, Class<? extends E> clazz) {
        return list.stream().anyMatch(clazz::isInstance);
    }

    public String fetchErrorMessage(Throwable throwable) {
        String errorMsg = Integer.toString(R.string.error_msg_unknown);

        if (!isNetworkConnected()) {
            errorMsg = context.getResources().getString(R.string.error_msg_no_internet);
        } else if (throwable instanceof TimeoutException) {
            errorMsg = context.getResources().getString(R.string.error_msg_timeout);
        }

        return errorMsg;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public static byte[] getDecoded(String data) {
        return Base64.getDecoder().decode(data);
    }

    // Remember to add android.permission.ACCESS_NETWORK_STATE permission.
    private boolean isNetworkConnected() {
        @SuppressLint("ServiceCast") ConnectivityManager connectivityManager = (ConnectivityManager) context.getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        return connectivityManager.getActiveNetworkInfo() != null;
    }
}
