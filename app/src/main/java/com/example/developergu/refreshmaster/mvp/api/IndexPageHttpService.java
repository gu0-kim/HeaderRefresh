package com.example.developergu.refreshmaster.mvp.api;

import java.util.List;

import io.reactivex.Observable;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import retrofit2.http.GET;

/** Created by developergu on 2018/1/19. */
public interface IndexPageHttpService {

  // 带header的返回结果Response<T>
  // 如果不需要header的结果使用Observable<T>
  @GET("/xxx")
  Observable<ResponseBody> getIndexPageData();
}
