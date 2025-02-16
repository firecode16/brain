package com.brain.util;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import com.brain.R;
import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestBuilder;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import java.util.ArrayList;
import java.util.Base64;
import java.util.concurrent.TimeoutException;

public class Util {

    private final Context context;

    public static final String URL = "http://192.168.1.6:8081/api/";
    public static final String URL_PART = "multimedia/";
    public static final String VIDEO_MP4 = "video/mp4";
    public static final String AUDIO_MP3 = "audio/mp3";
    public static final String IMG_JPG = "image/jpg";

    public static int[] getTabIcon = {
            R.drawable.ic_home_rocket_50,
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

    public RequestBuilder<Drawable> loadImage(@NonNull String url) {
        return Glide.with(context).load(url).skipMemoryCache(true).diskCacheStrategy(DiskCacheStrategy.NONE).listener(new RequestListener<>() {
            @Override
            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                return false;
            }

            @Override
            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                return false;
            }
        });
    }
}
