package com.example.developergu.refreshmaster.mvp.view.indexpage.noappbar;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;

/** Created by developergu on 2018/1/17. */
public class CollapseAnimController {
  private ValueAnimator mValueAnimator;
  private boolean mCollapseAnimRunning;
  private static final int ANIM_DURATION = 200;

  CollapseAnimController() {
    mValueAnimator = new ValueAnimator();
    mValueAnimator.addListener(
        new AnimatorListenerAdapter() {
          @Override
          public void onAnimationEnd(Animator animation) {
            super.onAnimationEnd(animation);
            mCollapseAnimRunning = false;
          }
        });
  }

  protected void addUpdateListener(ValueAnimator.AnimatorUpdateListener listener) {
    mValueAnimator.addUpdateListener(listener);
  }

  protected void setCollapseAnimValues(int startInt, int endInt) {
    mValueAnimator.setIntValues(startInt, endInt);
  }

  protected void ifStarted2Cancel() {
    if (mValueAnimator.isRunning()) {
      mValueAnimator.cancel();
    }
  }

  protected boolean isCollapseAnimRunning() {
    return mCollapseAnimRunning;
  }

  protected void startCollapseAnim() {
    startCollapseAnim(ANIM_DURATION);
    mCollapseAnimRunning = true;
  }

  protected void startCollapseAnim(int dur) {
    setCollapseAnimDuration(dur).start();
  }

  protected void clearAnim() {
    if (mValueAnimator != null) {
      mValueAnimator.cancel();
      mValueAnimator.removeAllListeners();
      mValueAnimator.removeAllUpdateListeners();
    }
  }

  private ValueAnimator setCollapseAnimDuration(int duration) {
    return mValueAnimator.setDuration(duration);
  }
}
