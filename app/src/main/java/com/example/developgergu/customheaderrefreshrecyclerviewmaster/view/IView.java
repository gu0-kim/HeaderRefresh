package com.example.developgergu.customheaderrefreshrecyclerviewmaster.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;

import com.example.developgergu.customheaderrefreshrecyclerviewmaster.presenter.IPresenter;

/** Created by developgergu on 2018/1/16. */
public interface IView<P extends IPresenter> {
  void initView(Context context, View parent, LayoutInflater inflater);

  void clear();

  void setPresenter(P presenter);
}
