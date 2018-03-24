package com.example.developergu.refreshmaster.di.component;

import com.example.developergu.refreshmaster.di.module.CustomModule;
import com.example.developergu.refreshmaster.di.scope.CustomScope;
import com.example.developergu.refreshmaster.mvp.view.indexpage.ZhiFuBaoIndexView;

import dagger.Component;

@CustomScope
@Component(modules = CustomModule.class, dependencies = AppComponent.class)
public interface ActivityComponent {
  void inject(ZhiFuBaoIndexView view);
}
