package com.developergu.headerrefresh.header.defaultype;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.view.View;

import com.developergu.headerrefresh.DimensUtils;

/** Created by developgergu on 2017/12/20. */
public class ArcProgressView extends View {

  private static final int START_ANGLE = -120;
  private static final int SWEEP_ANGLE = -300;
  private static final int DEFAULT_SIZE = 30;
  private static final int REFRESHING_START_ANGLE = -90;
  private static final int REFRESHING_SWEEP_ANGLE = -360;
  private static final int RING_DEFAULT_SIZE = 2;
  private int mStart, mSweep;
  private float ringWidth;
  private int ringMax;
  private Paint mPaint;
  private RectF mRectF;
  private int progress;
  private boolean measured;
  private RefreshingController mRefreshingController;
  private static final String TAG = ArcProgressView.class.getName();

  public ArcProgressView(Context context) {
    this(context, null);
  }

  public ArcProgressView(Context context, AttributeSet attrs) {
    this(context, attrs, 0);
  }

  public ArcProgressView(Context context, AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
    init();
  }

  private void init() {
    setWholeStyle(false);
    mRefreshingController = new RefreshingController();
    ringWidth = RING_DEFAULT_SIZE;
    mPaint = new Paint();
    mPaint.setAntiAlias(true);
    mPaint.setColor(Color.GRAY);
    mPaint.setStrokeWidth(ringWidth);
    // 设置画笔样式
    mPaint.setStyle(Paint.Style.STROKE);
  }

  public void setRingProgressColor(int color) {
    if (mPaint != null) {
      mPaint.setColor(color);
    }
  }

  public void setRingWidth(int width) {
    ringWidth = width;
    if (mPaint != null) {
      mPaint.setStrokeWidth(width);
    }
  }

  private void setWholeStyle(boolean isWhole) {
    mStart = isWhole ? REFRESHING_START_ANGLE : START_ANGLE;
    mSweep = isWhole ? REFRESHING_SWEEP_ANGLE : SWEEP_ANGLE;
  }

  @Override
  protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
    super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    if (!measured) {
      measured = true;
      int widthMode = MeasureSpec.getMode(widthMeasureSpec);
      int widthSize = MeasureSpec.getSize(widthMeasureSpec);
      int heightMode = MeasureSpec.getMode(heightMeasureSpec);
      int heightSize = MeasureSpec.getSize(heightMeasureSpec);
      int width, height;
      if (widthMode == MeasureSpec.AT_MOST) width = DimensUtils.dp2px(getContext(), DEFAULT_SIZE);
      else width = widthSize;
      if (heightMode == MeasureSpec.AT_MOST) height = DimensUtils.dp2px(getContext(), DEFAULT_SIZE);
      else height = heightSize;
      getLayoutParams().width = width;
      getLayoutParams().height = height;
      int centerX = width / 2;
      int centerY = height / 2;
      int radius = (int) (centerX - ringWidth / 2);
      mRectF = new RectF(centerX - radius, centerY - radius, centerX + radius, centerY + radius);
      setMeasuredDimension(width, height);
    }
  }

  @Override
  public void draw(Canvas canvas) {
    super.draw(canvas);
    drawProgress(canvas);
  }

  /** 绘制进度条 */
  private void drawProgress(Canvas canvas) {
    // 绘制圆弧
    canvas.drawArc(mRectF, mStart, mSweep * progress / ringMax, false, mPaint);
  }

  /** 设置进度 */
  public synchronized void setProgress(int progress) {
    if (progress < 0) {
      progress = 0;
    }
    if (progress >= ringMax) {
      progress = ringMax;
    }
    this.progress = progress;
    postInvalidate();
  }

  public void setRingMax(int max) {
    this.ringMax = max;
  }

  public void refreshing() {
    mRefreshingController.prepare();
    ViewCompat.postOnAnimation(this, mRefreshingController);
  }

  public void stop() {
    mRefreshingController.stop();
    setProgress(ringMax);
  }

  public void forceStop() {
    mRefreshingController.stop();
  }

  class RefreshingController implements Runnable, ValueAnimator.AnimatorUpdateListener {

    ValueAnimator mAnimator;

    RefreshingController() {
      mAnimator = new ValueAnimator();
      mAnimator.addUpdateListener(this);
      mAnimator.setDuration(2000);
    }

    void prepare() {
      setWholeStyle(true);
      mAnimator.setRepeatCount(ValueAnimator.INFINITE);
      mAnimator.setRepeatMode(ValueAnimator.RESTART);
      mAnimator.setIntValues(0, ringMax);
    }

    @Override
    public void run() {
      mAnimator.start();
    }

    void stop() {
      mAnimator.cancel();
      setWholeStyle(false);
    }

    @Override
    public void onAnimationUpdate(ValueAnimator animation) {
      setProgress((Integer) animation.getAnimatedValue());
    }
  }
}
