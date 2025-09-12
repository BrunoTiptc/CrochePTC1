package com.example.crocheptc;

import android.app.Application;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;

public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        // Inicializa o SDK do Facebook
        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);
    }
}
