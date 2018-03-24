package com.example.developergu.refreshmaster.mvp.model;

import com.example.developergu.refreshmaster.mvp.api.IndexPageHttpService;
import com.gu.mvp.model.IModel;

import io.reactivex.Observable;
import okhttp3.ResponseBody;
import retrofit2.Retrofit;

public class IndexPageModel extends IModel {
  private Retrofit mRetrofit;

  public IndexPageModel(Retrofit retrofit) {
    this.mRetrofit = retrofit;
  }

  public Observable<ResponseBody> getData() {
    return mRetrofit.create(IndexPageHttpService.class).getIndexPageData();
  }
}
