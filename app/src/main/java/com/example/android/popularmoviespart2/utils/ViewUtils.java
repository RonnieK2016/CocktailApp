package com.example.android.popularmoviespart2.utils;

import android.graphics.Color;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.widget.TextView;

import com.example.android.popularmoviespart2.R;

/**
 * Created by angelov on 6/18/2018.
 */

public final class ViewUtils {

    public static void showNoInternetConnectionSnackBar(View layoutToShow, View.OnClickListener callbackListener) {
        Snackbar snackbar = Snackbar
                .make(layoutToShow, R.string.no_internet_connect, Snackbar.LENGTH_INDEFINITE)
                .setAction("RETRY", callbackListener);
        snackbar.setActionTextColor(Color.RED);

        View snackbarView = snackbar.getView();
        TextView textView = snackbarView.findViewById(android.support.design.R.id.snackbar_text);
        textView.setTextColor(Color.YELLOW);
        snackbar.show();

    }
}
