package com.example.developergu.refreshmaster.di.component;

import com.example.developergu.refreshmaster.di.module.AppModule;
import com.example.developergu.refreshmaster.mvp.view.indexpage.ZhiFuBaoIndexView;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = AppModule.class)
public interface AppComponent {

  void inject(ZhiFuBaoIndexView view);
}
