package com.gu.mvp.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.gu.mvp.presenter.IPresenter;
import com.gu.mvp.view.fragment.BaseDialogFragment;

import javax.inject.Inject;

/**
 * normal dialog view
 *
 * @param <T> presenter
 */
public abstract class IDialogView<T extends IPresenter> extends BaseDialogFragment
    implements InjectView {

  @Inject protected T presenter;

  @SuppressWarnings("unchecked")
  public void setPresenter(T presenter) {
    this.presenter = presenter;
    presenter.setView(this);
  }

  public T getPresenter() {
    return presenter;
  }

  @Nullable
  @Override
  public View onCreateView(
      @NonNull LayoutInflater inflater,
      @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    injectPresenter();
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

  public void dismissDialog(int result, Intent data) {
    Fragment fragment = getTargetFragment();
    if (fragment != null) {
      fragment.onActivityResult(getTargetRequestCode(), result, data);
    }
    dismiss();
  }
}
