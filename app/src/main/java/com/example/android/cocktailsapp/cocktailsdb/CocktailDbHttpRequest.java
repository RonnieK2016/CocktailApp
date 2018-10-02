package com.example.android.cocktailsapp.cocktailsdb;

import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;
import com.example.android.cocktailsapp.deserializers.CocktailJsonDeserializer;
import com.example.android.cocktailsapp.domain.Cocktail;
import com.example.android.cocktailsapp.listeners.HttpResponseListener;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import org.apache.commons.lang3.exception.ExceptionUtils;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.util.Map;


public class CocktailDbHttpRequest extends Request<CocktailDbHttpResponse<Cocktail>> {

    private static final String TAG = CocktailDbHttpRequest.class.getSimpleName();
    private Map<String, String> mParams;
    private HttpResponseListener<CocktailDbHttpResponse<Cocktail>> mListener;
    private GsonBuilder gsonBuilder;

    public CocktailDbHttpRequest(int method, String url, Map<String, String> params, HttpResponseListener<CocktailDbHttpResponse<Cocktail>> listener) {
        super(method, url, listener);
        this.mParams = params;
        this.mListener = listener;
        gsonBuilder = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss");
        gsonBuilder.registerTypeAdapter(Cocktail.class, new CocktailJsonDeserializer());
        setRetryPolicy(new DefaultRetryPolicy(5000, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        setShouldCache(false);
    }

    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        return mParams;
    }

    @Override
    protected Response<CocktailDbHttpResponse<Cocktail>> parseNetworkResponse(NetworkResponse response) {
        try {
            String jsonResponse = new String(response.data,
                    HttpHeaderParser.parseCharset(response.headers));
            Gson gSonParser = gsonBuilder.create();

            TypeToken<CocktailDbHttpResponse<Cocktail>> typeToken = new TypeToken<CocktailDbHttpResponse<Cocktail>>(){};
            CocktailDbHttpResponse<Cocktail> parsedJsonResponse = gSonParser.fromJson(jsonResponse, typeToken.getType());

            return Response.success(parsedJsonResponse, HttpHeaderParser.parseCacheHeaders(response));
        } catch (UnsupportedEncodingException e) {
            Log.e(TAG, ExceptionUtils.getStackTrace(e));
            return null;
        }
    }

    @Override
    protected void deliverResponse(CocktailDbHttpResponse<Cocktail> response) {
        if(mListener != null) {
            mListener.onResponse(response);
        }
    }
}
