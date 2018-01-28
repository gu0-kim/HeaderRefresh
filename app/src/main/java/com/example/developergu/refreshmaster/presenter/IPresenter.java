package com.example.developergu.refreshmaster.presenter;

import com.example.developergu.refreshmaster.model.ModelResult;
import com.example.developergu.refreshmaster.view.IView;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/** Created by developergu on 2018/1/16. */
public abstract class IPresenter<V extends IView, T> {
  private CompositeDisposable mCompositeDisposable;
  private V view;

  IPresenter() {
    mCompositeDisposable = new CompositeDisposable();
  }

  public void bindView(V view) {
    this.view = view;
  }

  public V getView() {
    return view;
  }

  public void releaseView() {
    stopAllTask();
    view.clear();
    view = null;
  }

  public abstract Observable<ModelResult<T>> getModeTask();

  void startModeTask(
      Observable<ModelResult<T>> task,
      Consumer<ModelResult<T>> onFinish,
      Consumer<Throwable> onError,
      Action onDispose) {
    if (mCompositeDisposable != null)
      mCompositeDisposable.add(
          task.subscribeOn(Schedulers.io())
              .observeOn(AndroidSchedulers.mainThread())
              .doOnDispose(onDispose)
              .subscribe(onFinish, onError));
  }

  void stopAllTask() {
    if (!mCompositeDisposable.isDisposed()) mCompositeDisposable.dispose();
    mCompositeDisposable = null;
  }
}
