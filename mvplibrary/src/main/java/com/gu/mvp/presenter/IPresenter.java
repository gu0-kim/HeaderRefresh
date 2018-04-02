package com.gu.mvp.presenter;

import android.util.Log;

import com.gu.mvp.view.fragment.IFragment;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

public abstract class IPresenter<T extends IFragment> {
  protected T view;
  private CompositeDisposable mDisposables;

  public IPresenter() {
    initRx();
  }

  public void setView(T view) {
    this.view = view;
  }

  public void releaseView() {
    Log.e("TAG", view.getLogTagName() + "----presenter releaseView: ");
    this.view = null;
    disposeRx();
  }

  public void clearAllStartingTask() {
    if (mDisposables != null) {
      mDisposables.clear();
    }
  }

  public void addTask(Disposable disposable) {
    mDisposables.add(disposable);
  }

  private void initRx() {
    mDisposables = new CompositeDisposable();
  }

  private void disposeRx() {
    if (mDisposables != null && !mDisposables.isDisposed()) {
      mDisposables.dispose();
    }
    mDisposables = null;
  }
}
