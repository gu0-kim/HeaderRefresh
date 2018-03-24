package com.example.developergu.refreshmaster.di.module;

import com.example.developergu.refreshmaster.di.scope.CustomScope;
import com.example.developergu.refreshmaster.mvp.model.IndexPageModel;
import com.example.developergu.refreshmaster.mvp.presenter.IndexPagePresenter;

import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;

@Module
public class CustomModule {

  @Provides
  IndexPagePresenter getIndexPagePresenter(IndexPageModel model) {
    return new IndexPagePresenter(model);
  }

  @Provides
  IndexPageModel getIndexPageModel(Retrofit retrofit) {
    return new IndexPageModel(retrofit);
  }
}
