package com.example.developgergu.customheaderrefreshrecyclerviewmaster.model;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;
import io.reactivex.functions.Function;
import retrofit2.Response;
import retrofit2.adapter.rxjava2.Result;

/** Created by developgergu on 2018/1/16. */
public class IndexPageHttpModel extends IModel<List<String>> {

  private AnalogDelayOperation delayOp;

  public IndexPageHttpModel() {
    delayOp = new AnalogDelayOperation();
  }

  @Override
  public IndexPageHttpModel createTask() {
    if (mObservable == null) mObservable = convertResult(getSource());
    return this;
  }

  // 由网络返回的结果数据类型 转换 统一结果类型
  protected Observable<EntityResult<List<String>>> convertResult(
      Observable<Result<List<String>>> observable) {
    return observable.map(
        new Function<Result<List<String>>, EntityResult<List<String>>>() {
          @Override
          public EntityResult<List<String>> apply(Result<List<String>> listResult)
              throws Exception {
            if (listResult.isError())
              return new EntityResult<>(-1, listResult.error().getMessage());
            return new EntityResult<>(listResult.response().body());
          }
        });
  }

  // 模拟接口Retrofit接口，Retrofit返回Observable<Result<T>>类型
  private Observable<Result<List<String>>> getSource() {
    return Observable.range(0, 1)
        .map(
            new Function<Integer, String>() {
              @Override
              public String apply(Integer integer) throws Exception {
                return String.valueOf(integer);
              }
            })
        .toList()
        .toObservable()
        .compose(delayOp)
        .map(
            new Function<List<String>, Result<List<String>>>() {
              @Override
              public Result<List<String>> apply(List<String> strings) throws Exception {
                if (strings == null || strings.isEmpty()) {
                  Log.e("TAG", "apply: strings == null!!!!!!!!!!!!!");
                  return Result.error(new Throwable("超时了"));
                } else {
                  return Result.response(Response.success(strings));
                }
              }
            });
  }

  // 模拟网络延迟操作
  class AnalogDelayOperation implements ObservableTransformer<List<String>, List<String>> {
    @Override
    public ObservableSource<List<String>> apply(Observable<List<String>> upstream) {
      return upstream
          .delay(3, TimeUnit.SECONDS)
          .timeout(
              4,
              TimeUnit.SECONDS,
              Observable.create(
                  new ObservableOnSubscribe<List<String>>() {
                    @Override
                    public void subscribe(ObservableEmitter<List<String>> e) throws Exception {
                      e.onNext(new ArrayList<String>());
                      e.onComplete();
                    }
                  }));
    }
  }
}
