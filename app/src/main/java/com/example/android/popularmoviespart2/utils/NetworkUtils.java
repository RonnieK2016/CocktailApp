package com.example.android.popularmoviespart2.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;

import android.util.Log;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;

import java.util.Map;

/**
 * Created by angelov on 5/2/2018.
 */

public class NetworkUtils {

    public static final String TAG = NetworkUtils.class.getSimpleName();

    public static String buildUri(String baseUrl, Map<String, String> params) {
        Uri.Builder urlBuilder = Uri.parse(baseUrl).buildUpon();
        if(!MapUtils.isEmpty(params)) {
            for(Map.Entry<String, String> entry : params.entrySet()) {
                urlBuilder.appendQueryParameter(entry.getKey(), entry.getValue());
            }
        }
        return urlBuilder.build().toString();
    }

    public static boolean isConnectionAvailable(Context context) {
        try {
            ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo netInfo = cm.getActiveNetworkInfo();
            return netInfo != null;
        }
        catch(Exception e){
            Log.e(TAG, ExceptionUtils.getStackTrace(e));
            return false;
        }
    }

}
