package com.example.developergu.refreshmaster.app;

import android.app.Application;
import android.os.StrictMode;

import com.example.developergu.refreshmaster.di.component.AppComponent;
import com.example.developergu.refreshmaster.di.component.DaggerAppComponent;
import com.squareup.leakcanary.LeakCanary;
import com.squareup.leakcanary.RefWatcher;

/** Created by developergu on 2018/1/16. */
public class MyApplication extends Application {
  private RefWatcher mRefWatcher;
  private AppComponent mAppComponent;

  public RefWatcher getRefWatcher() {
    return mRefWatcher;
  }

  @Override
  public void onCreate() {
    super.onCreate();
    if (LeakCanary.isInAnalyzerProcess(this)) {
      // This process is dedicated to LeakCanary for heap analysis.
      // You should not init your app in this process.
      return;
    }
    enabledStrictMode();
    mRefWatcher = LeakCanary.install(this);
    mAppComponent = DaggerAppComponent.builder().build();
  }

  private static void enabledStrictMode() {
    StrictMode.setThreadPolicy(
        new StrictMode.ThreadPolicy.Builder() //
            .detectAll() //
            .penaltyLog() //
            .penaltyDeath() //
            .build());
  }

  public AppComponent getAppComponent() {
    return mAppComponent;
  }
}
