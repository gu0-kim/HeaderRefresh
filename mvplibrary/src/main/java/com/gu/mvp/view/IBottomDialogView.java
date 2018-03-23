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
import com.gu.mvp.view.fragment.BaseBottomSheetDialogFragment;

import javax.inject.Inject;

/**
 * bottom dialog view
 *
 * @param <T> presenter
 */
public abstract class IBottomDialogView<T extends IPresenter> extends BaseBottomSheetDialogFragment
    implements InjectView {

  @Inject protected T presenter;

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
    dismiss();
    Fragment fragment = getTargetFragment();
    if (fragment != null) {
      fragment.onActivityResult(getTargetRequestCode(), result, data);
    }
  }
}
