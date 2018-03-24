package com.example.developergu.refreshmaster.di.module;

import java.util.concurrent.TimeUnit;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

@Module
public class AppModule {
  private static final String BASE_URL = "http://www.google.com";

  @Provides
  @Singleton
  OkHttpClient provideOkHttpClient() {
    return new OkHttpClient.Builder()
        .connectTimeout(60 * 1000, TimeUnit.MILLISECONDS)
        .readTimeout(60 * 1000, TimeUnit.MILLISECONDS)
        .build();
  }

  @Provides
  @Singleton
  Retrofit provideRetrofit(OkHttpClient client) {
    return new Retrofit.Builder().client(client).baseUrl(BASE_URL).build();
  }
}
