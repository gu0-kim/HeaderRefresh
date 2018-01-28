package com.example.developergu.refreshmaster.model;

import io.reactivex.Observable;

/*
 * Created by developgergu on 2018/1/16;
 *  <T> is data Type.
 */
public abstract class IModel<T, D> {
  private Observable<ModelResult<T>> mObservable;

  public Observable<ModelResult<T>> createModeTask() {
    if (mObservable == null) {
      mObservable = convertResult(getSource());
    }
    return mObservable;
  }

  // 由网络返回的结果数据类型，转换统一结果类型（ <D>到ModelResult<T> 转换）
  public abstract Observable<ModelResult<T>> convertResult(Observable<D> observable);

  protected abstract Observable<D> getSource();
}
