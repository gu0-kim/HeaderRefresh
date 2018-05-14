package com.example.developergu.refreshmaster.di.component;

/** Created by devel on 2018/5/13. */
public class ComponentController {
  private static ComponentController instance;
  private AppComponent mComponent;

  public static ComponentController getInstance() {
    if (instance == null) {
      instance = new ComponentController();
    }
    return instance;
  }

  private ComponentController() {
    mComponent = DaggerAppComponent.builder().build();
  }

  public AppComponent getComponent() {
    return mComponent;
  }

  public void release() {
    instance = null;
    mComponent = null;
  }
}
