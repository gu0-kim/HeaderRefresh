package com.example.developergu.refreshmaster.di.component;

import com.example.developergu.refreshmaster.di.module.AppModule;

import javax.inject.Singleton;

import dagger.Component;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;

@Singleton
@Component(modules = AppModule.class)
public interface AppComponent {
  Retrofit getRetrofit();

  OkHttpClient getOkHttpClient();
}
