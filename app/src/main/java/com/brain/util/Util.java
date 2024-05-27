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

    public static final String URL = "http://192.168.1.121:8081/api/";
    public static final String URL_PART = "multimedia/";
    public static final String VIDEO_MP4 = "video/mp4";
    public static final String AUDIO_MP3 = "audio/mp3";
    public static final String IMG_JPG = "image/jpg";

    public static int[] getTabIcon = {
            R.drawable.ic_home_public,
            R.drawable.ic_chat,
            R.drawable.ic_setting_50
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
