package com.example.androidthings.simpleui;

import android.app.Activity;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import  android.os.IGpioService;

import java.lang.reflect.Method;

public class MainActivity extends Activity {

    IGpioService gpioService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initGpio();
        Log.e("count", gpioService.gpioGetNumber());

    }

    private void initGpio() {
        Method method = null;

        try {
            method = Class.forName("android.os.ServiceManager").getMethod("getService", String.class)
            IBinder binder = (IBinder) method.invoke(null, new Object[]{"gpio"});

            gpioService = IGpioService.Stub.asInterface(binder);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
