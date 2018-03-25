package com.example.developergu.refreshmaster.app;

import com.example.developergu.refreshmaster.di.component.AppComponent;
import com.example.developergu.refreshmaster.di.component.DaggerAppComponent;
import com.gu.mvp.app.BaseApp;

/** Created by developergu on 2018/3/16. */
public class MyApplication extends BaseApp<AppComponent> {

  @Override
  public AppComponent createComponent() {
    return DaggerAppComponent.builder().build();
  }
}
