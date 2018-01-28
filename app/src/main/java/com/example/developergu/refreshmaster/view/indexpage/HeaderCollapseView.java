package com.example.developergu.refreshmaster.view.indexpage;

import android.animation.ValueAnimator;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;

import com.example.developergu.refreshmaster.presenter.IPresenter;
import com.example.developergu.refreshmaster.view.IView;

/** Created by developgergu on 2018/1/17. */
public abstract class HeaderCollapseView<V extends View, P extends IPresenter> extends IView<P>
    implements CollapsableItem<V> {
  protected CollapseAnimController mCollapseAnimController;

  @Override
  public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
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
  }

  @Override
  public void onDestroyView() {
    super.onDestroyView();
    Log.e("TAG", "------onDestroyView-----");
    mCollapseAnimController.clearAnim();
    mCollapseAnimController = null;
  }

  @Override
  public void onDestroy() {
    super.onDestroy();
    Log.e("TAG", "------onDestroy-----");
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
