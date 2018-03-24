package com.gu.mvp.view;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.gu.mvp.presenter.IPresenter;
import com.gu.mvp.view.fragment.BaseFragment;

import javax.inject.Inject;

public abstract class IView<T extends IPresenter> extends BaseFragment implements InjectView {
  @Inject protected T presenter;

  @SuppressWarnings("unchecked")
  public void setPresenter(T presenter) {
    this.presenter = presenter;
    presenter.setView(this);
  }

  public T getPresenter() {
    return presenter;
  }

  @SuppressWarnings("unchecked")
  @Nullable
  @Override
  public View onCreateView(
      @NonNull LayoutInflater inflater,
      @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    injectPresenter();
    presenter.setView(this);
    return super.onCreateView(inflater, container, savedInstanceState);
  }

  @Override
  public void onDestroyView() {
    super.onDestroyView();
    if (presenter != null) {
      presenter.releaseView();
      presenter = null;
    }
  }

  @Override
  public void onDestroy() {
    super.onDestroy();
  }
}
