package com.gu.mvp.view.fragment;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.view.View;

/** fragment相关方法 */
public interface IFragment {

  /*class name that use in log*/
  String getLogTagName();

  @LayoutRes
  int getLayoutId();

  /*call in onCreateView()*/
  void initView(View parent);

  /*call in onDestroyView()*/
  void destroyView();

  void showToast(Context context, String toast);
}
