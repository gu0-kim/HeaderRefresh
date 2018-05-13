package com.example.developergu.refreshmaster.mvp.api;

import io.reactivex.Observable;
import okhttp3.ResponseBody;
import retrofit2.http.GET;

/** Created by developergu on 2018/1/19. */
public interface IndexPageHttpService {

  // 模拟一个网络请求，实际不存在，进行超时处理
  // 带header的返回结果Response<T>
  // 如果不需要header的结果使用Observable<T>
  @GET("test/data.html")
  Observable<ResponseBody> getIndexPageData();
}
