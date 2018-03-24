package com.example.developergu.refreshmaster.di.module;

import com.example.developergu.refreshmaster.mvp.model.indexpage.IndexPagePresenter;

import dagger.Module;
import dagger.Provides;

@Module
public class CustomModule {

  @Provides
  IndexPagePresenter getIndexPagePresenter() {
    return new IndexPagePresenter();
  }
}
