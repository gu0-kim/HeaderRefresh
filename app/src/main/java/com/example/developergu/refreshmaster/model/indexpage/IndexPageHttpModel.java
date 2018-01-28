package com.example.developergu.refreshmaster.model.indexpage;

import com.example.developergu.refreshmaster.model.IModel;
import com.example.developergu.refreshmaster.model.ModelResult;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.functions.Function;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/** Created by developgergu on 2018/1/16. */
public class IndexPageHttpModel extends IModel<List<String>, Response<List<String>>> {

  private static final String BASE_URL = "http://www.google.com/";

  public IndexPageHttpModel() {}

  public Observable<Response<List<String>>> getSource() {
    return getRetrofit().create(IndexPageHttpService.class).getIndexPageData();
  }

  // 由网络返回的结果数据类型 转换 统一结果类型
  @Override
  public Observable<ModelResult<List<String>>> convertResult(
      Observable<Response<List<String>>> observable) {
    return observable.map(
        new Function<Response<List<String>>, ModelResult<List<String>>>() {
          @Override
          public ModelResult<List<String>> apply(Response<List<String>> response) throws Exception {
            return response.isSuccessful()
                ? new ModelResult<>(response.body())
                : new ModelResult<List<String>>(response.code(), response.message());
          }
        });
  }

  private Retrofit getRetrofit() {
    return new Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .build();
  }
}
