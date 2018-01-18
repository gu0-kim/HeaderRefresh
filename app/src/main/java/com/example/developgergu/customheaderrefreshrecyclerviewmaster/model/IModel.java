package com.example.developgergu.customheaderrefreshrecyclerviewmaster.model;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/*
 * Created by developgergu on 2018/1/16;
 *  <T> is data Type.
 */
public abstract class IModel<T> {
  protected Observable<EntityResult<T>> mObservable;

  public abstract IModel<T> createTask();
  //  public abstract Observable<EntityResult<T>> createTask();

  public Disposable load(Consumer<EntityResult<T>> result) {
    return mObservable
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(result);
  }
}
