package com.htcardone.baking;

import android.app.Application;

import com.facebook.stetho.Stetho;
import com.squareup.picasso.Picasso;

/**
 * Created by Henrique Cardone on 15/10/2017.
 */

public class BakingApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        if (BuildConfig.DEBUG) Stetho.initializeWithDefaults(this);
        Picasso.with(getApplicationContext()).setIndicatorsEnabled(BuildConfig.DEBUG);
    }
}
