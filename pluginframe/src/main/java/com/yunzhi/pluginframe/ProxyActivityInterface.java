package com.yunzhi.pluginframe;

import android.app.Activity;
import android.os.Bundle;
import android.view.MotionEvent;

public interface ProxyActivityInterface {

    //生命周期的activity

    public void attach(Activity proxyActivity);


    public void onCreate(Bundle savedInstanceState);

    public void onStart();

    public void onResume();

    public void onPause();

    public void onStop();

    public void onDestroy();

    public void onSaveInstanceState(Bundle outState);

    public boolean onTouchEvent(MotionEvent event);

    public void onBackPressed();
}