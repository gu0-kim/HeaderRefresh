package com.example.developgergu.customheaderrefreshrecyclerviewmaster.view.indexpage;

import android.animation.ValueAnimator;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.developgergu.customheaderrefreshrecyclerviewmaster.BaseFragment;
import com.example.developgergu.customheaderrefreshrecyclerviewmaster.R;
import com.example.developgergu.customheaderrefreshrecyclerviewmaster.presenter.IPresenter;
import com.example.developgergu.customheaderrefreshrecyclerviewmaster.view.IView;

/** Created by developgergu on 2018/1/17. */
public abstract class HeaderCollapseView<V extends View, P extends IPresenter> extends BaseFragment
    implements IView<P>, CollapsableItem<V> {
  protected CollapseAnimController mCollapseAnimController;
  protected P mPresenter;

  @Override
  public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
  }

  @Nullable
  @Override
  public View onCreateView(
      @NonNull LayoutInflater inflater,
      @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    View parent = inflater.inflate(R.layout.fragmen_layout, container, false);
    initView(getContext(), parent, inflater);
    mCollapseAnimController = new CollapseAnimController();
    mCollapseAnimController.addUpdateListener(
        new ValueAnimator.AnimatorUpdateListener() {
          @Override
          public void onAnimationUpdate(ValueAnimator valueAnimator) {
            scrollTo((int) valueAnimator.getAnimatedValue());
            setCurrentScrollY((int) valueAnimator.getAnimatedValue());
            onCollapseScroll((int) valueAnimator.getAnimatedValue());
          }
        });
    return parent;
  }

  @Override
  public void onDestroyView() {
    super.onDestroyView();
    mCollapseAnimController.clearAnim();
    mCollapseAnimController = null;
  }

  @Override
  public void onDestroy() {
    super.onDestroy();
    mPresenter.release();
    mPresenter = null;
  }

  public boolean isCollapsed(int curScroll) {
    return (int) (getCollapseRate(curScroll) + 0.5f) > 0;
  }

  public float getCollapseRate(int curScroll) {
    return 1f * curScroll / getMaxCollapse();
  }

  public boolean isCollapseAnimRunning() {
    return mCollapseAnimController.isCollapseAnimRunning();
  }
}
