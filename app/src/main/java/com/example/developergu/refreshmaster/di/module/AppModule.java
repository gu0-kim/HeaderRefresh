package com.example.developergu.refreshmaster.di.module;

import com.example.developergu.refreshmaster.mvp.model.IndexPageModel;
import com.example.developergu.refreshmaster.mvp.presenter.IndexPagePresenter;
import com.example.developergu.refreshmaster.mvp.presenter.SimplePresenter;

import java.util.concurrent.TimeUnit;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

@Module
public class AppModule {
  private static final String BASE_URL = "http://192.168.10.180:8080/";
  private static final int TIMEOUT = 3000;

  @Provides
  @Singleton
  OkHttpClient provideOkHttpClient() {
    return new OkHttpClient.Builder()
        .connectTimeout(TIMEOUT, TimeUnit.MILLISECONDS)
        .readTimeout(TIMEOUT, TimeUnit.MILLISECONDS)
        .build();
  }

  @Provides
  @Singleton
  Retrofit provideRetrofit(OkHttpClient client) {
    return new Retrofit.Builder()
        .client(client)
        .baseUrl(BASE_URL)
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .build();
  }

  @Provides
  IndexPagePresenter getIndexPagePresenter(IndexPageModel model) {
    return new IndexPagePresenter(model);
  }

  @Provides
  IndexPageModel getIndexPageModel(Retrofit retrofit) {
    return new IndexPageModel(retrofit);
  }

  @Provides
  SimplePresenter getSimplePresenter() {
    return new SimplePresenter();
  }
}
