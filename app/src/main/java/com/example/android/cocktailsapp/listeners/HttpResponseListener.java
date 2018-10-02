package com.example.android.cocktailsapp.listeners;

import com.android.volley.Response;

/**
 * Created by angelov on 5/2/2018.
 */
public interface HttpResponseListener<T> extends Response.Listener<T>, Response.ErrorListener {
}
