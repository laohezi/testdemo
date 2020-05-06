package com.yunzhi.hook;

import android.content.Context;
import android.util.Log;
import android.view.View;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class HookHelper {

    public static void hook(Context context, View view) {
        try {
            Method method = View.class.getDeclaredMethod("getListenerInfo");
            method.setAccessible(true);

            Object info = method.invoke(view);

            Class<?> infoClass = Class.forName("android.view.View$ListenerInfo");
            Field field = infoClass.getDeclaredField("mOnClickListener");

            final View.OnClickListener listenerInstance = (View.OnClickListener) field.get(info);

           Object proxy = Proxy.newProxyInstance(context.getClassLoader(), new Class[]{
                    View.OnClickListener.class
            }, new InvocationHandler() {
                @Override
                public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                    Log.d("Hoook", "beidianjidao;e");
                    return method.invoke(listenerInstance, args);
                }
            });

       //   Object proxy = new OnClickListenerProxy(listenerInstance);
            field.set(info, proxy);


        } catch (Exception e) {

        }


    }
}
