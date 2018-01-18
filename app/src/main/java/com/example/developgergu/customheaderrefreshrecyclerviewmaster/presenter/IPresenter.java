package com.example.developgergu.customheaderrefreshrecyclerviewmaster.presenter;

import com.example.developgergu.customheaderrefreshrecyclerviewmaster.model.IModel;
import com.example.developgergu.customheaderrefreshrecyclerviewmaster.view.IView;

import io.reactivex.disposables.CompositeDisposable;

/** Created by developgergu on 2018/1/16. */
public abstract class IPresenter<V extends IView, M extends IModel> {
  protected V view;
  protected M model;
  protected CompositeDisposable mCompositeDisposable;

  IPresenter() {
    mCompositeDisposable = new CompositeDisposable();
  }

  public void bindView(V view) {
    this.view = view;
  }

  public void release() {
    view.clear();
    view = null;
    model = null;
    if (!mCompositeDisposable.isDisposed()) mCompositeDisposable.dispose();
    mCompositeDisposable = null;
  }
}
