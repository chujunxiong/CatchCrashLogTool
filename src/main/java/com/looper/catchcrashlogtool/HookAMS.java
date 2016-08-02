package com.looper.catchcrashlogtool;

import android.content.Context;

import java.lang.reflect.Field;
import java.lang.reflect.Proxy;

/**
 * Created by looper on 2016/7/27.
 */
public class HookAMS implements  HookHandler {

    private  static HookAMS INSTANCE ;
    private Context context;
    private HookAMS(Context context) {
        this.context = context;
    }

    public static HookAMS getInstance(Context context) {
        if (INSTANCE == null) {
            INSTANCE = new HookAMS(context);
        }
        return INSTANCE;
    }

    @Override
    public void hookHandler() {
        try {
            Class<?> activityManagerNativeClass = Class.forName("android.app.ActivityManagerNative");

            Field gDefaultField = activityManagerNativeClass.getDeclaredField("gDefault");
            gDefaultField.setAccessible(true);
            Object gDefault = gDefaultField.get(null);

            Class<?> singleton = Class.forName("android.util.Singleton");
            Field mInstanceField = singleton.getDeclaredField("mInstance");
            mInstanceField.setAccessible(true);

            Object rawIActivityManager = mInstanceField.get(gDefault);

            Class<?> iActivityManagerInterface = Class.forName("android.app.IActivityManager");
            Object proxy = Proxy.newProxyInstance(Thread.currentThread().getContextClassLoader(),
                    new Class<?>[] { iActivityManagerInterface }, new IActivityManagerHandler(rawIActivityManager,context));
            mInstanceField.set(gDefault, proxy);

        } catch (Exception e) {
            throw new RuntimeException("Hook Failed", e);
        }
    }
}
