package com.lmj.www.lmjcustomview;

import android.app.Application;
import android.content.Context;

/**
 * Created by aus on 2016/11/24.
 */

public class BaseApplication extends Application{
    private static Application app;
    @Override
    public void onCreate() {
        super.onCreate();
        app =this;
    }

    public static   Context getAppContext() {
        return app;
    }
}
