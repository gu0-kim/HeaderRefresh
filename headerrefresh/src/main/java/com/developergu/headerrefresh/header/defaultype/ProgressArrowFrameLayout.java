package com.developergu.headerrefresh.header.defaultype;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.developergu.headerrefresh.R;

/** Created by developgergu on 2017/12/29. */
public class ProgressArrowFrameLayout extends FrameLayout {

  private static final int INVALID = -1;
  private ArcProgressView mProgressView;
  private ImageView mRotateImageView;
  private Direction mDirection = Direction.ARROW_DOWN;
  private RotateAnimation mArrowUpAnim, mArrowDownAnim;

  public enum Direction {
    ARROW_UP,
    ARROW_DOWN
  }

  public ProgressArrowFrameLayout(@NonNull Context context) {
    super(context);
    init(context, null);
  }

  public ProgressArrowFrameLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
    super(context, attrs);
    init(context, attrs);
  }

  public ProgressArrowFrameLayout(
      @NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
    init(context, attrs);
  }

  private void init(Context context, AttributeSet attrs) {
    int imgSrc = INVALID;
    int ringColor = Color.GRAY;
    int ringWidth = 5;
    if (attrs != null) {
      TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.ProgressArrowFrameLayout);
      if (ta != null) {
        imgSrc = ta.getResourceId(R.styleable.ProgressArrowFrameLayout_frameRingImage, INVALID);
        ringColor = ta.getColor(R.styleable.ProgressArrowFrameLayout_frameProgressColor, ringColor);
        ringWidth =
            ta.getDimensionPixelOffset(
                R.styleable.ProgressArrowFrameLayout_frameRingWidth, ringWidth);
        ta.recycle();
      }
    }
    rotateAnimInit();
    mProgressView = new ArcProgressView(context);
    mProgressView.setRingProgressColor(ringColor);
    mProgressView.setRingWidth(ringWidth);
    mRotateImageView = new ImageView(context);
    if (imgSrc != INVALID) {
      mRotateImageView.setImageResource(imgSrc);
      mRotateImageView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
    }
    addView(mProgressView, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
    addView(
        mRotateImageView, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
    rotateAnimInit();
  }

  public void setProgress(int progress) {
    mProgressView.setProgress(progress);
  }

  public void direction(Direction direction) {
    if (mDirection != direction) {
      mDirection =
          direction == Direction.ARROW_DOWN ? startArrowDownAnimation() : startArrowUpAnimation();
    }
  }

  public void setRingMax(int max) {
    mProgressView.setRingMax(max);
  }

  public void startRefresh() {
    mProgressView.refreshing();
    mRotateImageView.clearAnimation();
    mRotateImageView.setVisibility(View.GONE);
  }

  public void stopRefresh() {
    mRotateImageView.setVisibility(View.VISIBLE);
    mProgressView.stop();
    setVisibility(View.INVISIBLE);
  }

  public void forceStop() {
    mProgressView.forceStop();
  }

  private void rotateAnimInit() {
    if (mArrowUpAnim == null) {
      mArrowUpAnim =
          new RotateAnimation(
              0f, 180f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
      mArrowUpAnim.setFillAfter(true);
    }
    if (mArrowDownAnim == null) {
      mArrowUpAnim.setDuration(200);
      mArrowDownAnim =
          new RotateAnimation(
              180f, 360f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
      mArrowDownAnim.setDuration(200);
    }
  }

  private Direction startArrowDownAnimation() {
    mArrowUpAnim.cancel();
    mRotateImageView.startAnimation(mArrowDownAnim);
    return Direction.ARROW_DOWN;
  }

  private Direction startArrowUpAnimation() {
    mArrowDownAnim.cancel();
    mRotateImageView.startAnimation(mArrowUpAnim);
    return Direction.ARROW_UP;
  }

  public void clear() {
    mDirection = null;
  }
}
