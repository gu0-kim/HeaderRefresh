package com.example.developergu.refreshmaster.view;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;

import com.example.developergu.refreshmaster.BaseFragment;
import com.example.developergu.refreshmaster.presenter.IPresenter;

/** Created by developergu on 2018/1/16. */
public abstract class IView<P extends IPresenter> extends BaseFragment {
  public P mPresenter;
  /** if IView is a type of Fragment,calling the method from onCreateView() */
  public abstract void initView(Context context, View parent, LayoutInflater inflater);

  /** if IView is a type of Fragment,calling the method from onDestroyView() */
  public abstract void clear();

  public abstract void createPresenter();

  @Override
  public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    createPresenter();
  }

  @Override
  public void onDestroyView() {
    super.onDestroyView();
    mPresenter.releaseView();
  }

  @Override
  public void onDestroy() {
    super.onDestroy();
    mPresenter = null;
  }
}
