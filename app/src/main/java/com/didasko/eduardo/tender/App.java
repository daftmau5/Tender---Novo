package com.didasko.eduardo.tender;

import android.app.Application;
import android.content.Context;

import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;

/**
 * Created by Tecnico_Tarde on 01/04/2016.
 */
public class App extends Application {
    private static Context contexto;
    @Override
    public void onCreate() {
        super.onCreate();
        contexto = this;
        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);
    }

    public static Context getContexto() {
        return contexto;
    }
}
