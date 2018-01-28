package com.example.developergu.refreshmaster.model.indexpage;

import com.example.developergu.refreshmaster.model.IModel;
import com.example.developergu.refreshmaster.model.ModelResult;

import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;
import io.reactivex.functions.Function;

/** Created by developgergu on 2018/1/16. */
public class IndexPageLocalModel extends IModel<List<String>, List<String>> {

  private AnalogDelayOperation delayOp;

  public IndexPageLocalModel() {
    delayOp = new AnalogDelayOperation();
  }

  // 模拟接口Retrofit接口，Retrofit返回Observable<Result<T>>类型
  public Observable<List<String>> getSource() {
    return searchFromDataBase();
  }

  // 由网络返回的结果数据类型 转换 统一结果类型
  @Override
  public Observable<ModelResult<List<String>>> convertResult(Observable<List<String>> observable) {
    return observable.map(
        new Function<List<String>, ModelResult<List<String>>>() {
          @Override
          public ModelResult<List<String>> apply(List<String> listResult) throws Exception {
            return new ModelResult<>(listResult);
          }
        });
  }

  // 模拟一个本地查询任务
  private Observable<List<String>> searchFromDataBase() {
    return Observable.range(0, 10)
        .map(
            new Function<Integer, String>() {
              @Override
              public String apply(Integer integer) throws Exception {
                return String.valueOf(integer);
              }
            })
        .toList()
        .toObservable()
        .compose(delayOp);
  }

  // 模拟任务延迟操作
  class AnalogDelayOperation implements ObservableTransformer<List<String>, List<String>> {
    private static final int DELAY_SEC = 1;
    private static final int TIMEOUT_SEC = 3;

    @Override
    public ObservableSource<List<String>> apply(Observable<List<String>> upstream) {
      return upstream.delay(DELAY_SEC, TimeUnit.SECONDS).timeout(TIMEOUT_SEC, TimeUnit.SECONDS);
    }
  }
}
