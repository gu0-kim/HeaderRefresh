package com.developergu.headerrefresh.header;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.developergu.headerrefresh.HeaderRefreshRecyclerView.PullState;
import com.developergu.headerrefresh.R;

/** Created by developgergu on 2017/12/18. */
public abstract class RefreshLayout extends LinearLayout implements RefreshableView {
  protected static final int INVALID_LAYOUT_ID = -1;
  private boolean init;
  protected ViewGroup mRefreshEntityView;

  /*初始高度，默认是RefreshFixableLayout高度*/
  protected int LAYOUT_INIT_SIZE;

  /*最大下拉距离,该值来自xml文件height*/
  protected int MAX_PULL_SIZE;

  /*下拉距离到达该值后，释放可刷新*/
  protected int REFRESH_NEED_PULL_SIZE;

  /*到达刷新距离时整个RefreshLayout高度，值为REFRESH_NEED_PULL_SIZE+LAYOUT_INIT_SIZE*/
  protected int REFRESH_HEIGHT;

  /*HeaderLayout的最大高度*/
  private int MAX_HEIGHT;

  /*当前RefreshLayout高度*/
  private int mHeight;

  protected PullState mState;

  /** builder config */

  /*下拉和实际移动比率*/
  protected float mPullRate;

  /*比率越大，到达刷新距离后能移动的距离越大*/
  protected float mHeaderPullOverRate;

  /*id指向refreshLayout的固定头部*/
  protected int mRefreshFixableLayoutId;

  protected int mAnimVelocity;

  /*下拉距离变化监听*/
  protected HeaderOffsetListener mOffsetListener;

  protected abstract void initRefreshLayout(Context context);

  protected abstract void configRefreshLayout();

  protected abstract void setStatePull();

  protected abstract void setStateRelease2Refresh();

  protected abstract void setStateRefreshing();

  public RefreshLayout(Context context) {
    super(context);
  }

  public RefreshLayout(Context context, @Nullable AttributeSet attrs) {
    super(context, attrs);
  }

  public RefreshLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
  }

  public interface HeaderOffsetListener {
    /**
     * @param offset current offset
     * @param refreshDimen beyond the size need to be refresh
     * @param maxOffset the max size of pull
     */
    void onOffsetChanged(int offset, int refreshDimen, int maxOffset);
  }

  public void setOffsetListener(HeaderOffsetListener offsetListener) {
    this.mOffsetListener = offsetListener;
  }

  protected void init(Context context, AttributeSet attrs) {
    init = true;
    if (attrs != null) {
      TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.RefreshLayout);
      if (ta != null) {
        mRefreshFixableLayoutId =
            ta.getResourceId(R.styleable.RefreshLayout_fixable_layout, INVALID_LAYOUT_ID);
        mPullRate = ta.getFloat(R.styleable.RefreshLayout_pull_distance_rate, mPullRate);
        mHeaderPullOverRate =
            ta.getFloat(R.styleable.RefreshLayout_header_refresh_rate, mHeaderPullOverRate);
        ta.recycle();
      }
    } else {
      // instance comes from "new"  , not from "XML" ,default is HORIZONTAL
      setOrientation(VERTICAL);
      setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
    }
    addView(
        LayoutInflater.from(context)
            .inflate(
                mRefreshFixableLayoutId != INVALID_LAYOUT_ID
                    ? mRefreshFixableLayoutId
                    : R.layout.refresh_fixable_layout_default,
                this,
                false));

    initRefreshLayout(context);
    addView(mRefreshEntityView);
  }

  @Override
  protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
    super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    if (init) {
      init = false;
      View fixableLayout = getChildAt(0);
      mHeight = LAYOUT_INIT_SIZE = fixableLayout.getMeasuredHeight();
      /*测量mRefreshRate是否在合理范围内*/
      // 测量内容大小
      mRefreshEntityView.measure(
          widthMeasureSpec, MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
      REFRESH_NEED_PULL_SIZE = mRefreshEntityView.getMeasuredHeight();
      REFRESH_HEIGHT = LAYOUT_INIT_SIZE + REFRESH_NEED_PULL_SIZE;
      MAX_PULL_SIZE = (int) (REFRESH_NEED_PULL_SIZE * (1f + mHeaderPullOverRate));
      MAX_HEIGHT = LAYOUT_INIT_SIZE + MAX_PULL_SIZE;
      configRefreshLayout();
      // 设置layout params高度，防止多次measure时变化
      mRefreshEntityView.getLayoutParams().height = MAX_PULL_SIZE;
      // 还原mRefreshLayout高度
      mRefreshEntityView.measure(
          widthMeasureSpec, MeasureSpec.makeMeasureSpec(MAX_PULL_SIZE, MeasureSpec.EXACTLY));
    }
    setMeasuredDimension(MeasureSpec.getSize(widthMeasureSpec), mHeight);
  }

  @Override
  public void moveBy(int deltaY) {
    if (deltaY < 0 && mHeight >= LAYOUT_INIT_SIZE && mHeight < MAX_HEIGHT) {
      update(Math.min(mHeight - deltaY, MAX_HEIGHT));
    } else if (deltaY > 0 && mHeight > LAYOUT_INIT_SIZE) {
      update(Math.max(LAYOUT_INIT_SIZE, mHeight - deltaY));
    }
  }

  @Override
  public void moveTo(int height) {
    moveBy(mHeight - height);
  }

  @Override
  public void refreshing() {
    mState = PullState.REFRESHING;
  }

  @Override
  public void reset() {
    ViewGroup.LayoutParams params = getLayoutParams();
    params.height = LAYOUT_INIT_SIZE;
    setLayoutParams(params);
  }

  @Override
  public void stopRefreshing() {
    mState = PullState.RELEASE_TO_REFRESH;
  }

  @Override
  public void forceStop() {
    mState = PullState.PULL;
  }

  public int getAnimVelocity() {
    return mAnimVelocity;
  }

  private void update(int newH) {
    if (newH != mHeight) {
      updateHeight(newH);
      updateRefreshLayout(newH, mHeight - newH);
      mHeight = newH;
    }
  }

  private void updateHeight(int newH) {
    ViewGroup.LayoutParams params = getLayoutParams();
    params.height = newH;
    setLayoutParams(params);
    if (mOffsetListener != null) {
      mOffsetListener.onOffsetChanged(
          newH - LAYOUT_INIT_SIZE, REFRESH_NEED_PULL_SIZE, MAX_PULL_SIZE);
    }
  }

  protected void updateRefreshLayout(int newH, int deltaY) {
    PullState newState = calculateState(newH);
    if (newState == PullState.PULL && mState == PullState.RELEASE_TO_REFRESH) {
      setStatePull();
    } else if (newState == PullState.RELEASE_TO_REFRESH && mState == PullState.PULL) {
      setStateRelease2Refresh();
    } else if (newState == PullState.PULL && mState == PullState.REFRESHING) {
      setStatePull();
    }
    mState = newState;
  }

  protected PullState calculateState(int newH) {
    return (newH > LAYOUT_INIT_SIZE && newH < REFRESH_HEIGHT)
        ? PullState.PULL
        : (newH >= REFRESH_HEIGHT) ? PullState.RELEASE_TO_REFRESH : PullState.IDLE;
  }

  public boolean pullVisible() {
    return getLayoutParams().height > LAYOUT_INIT_SIZE;
  }

  public PullState getCurrentState() {
    return mState;
  }

  public int getLayoutInitSize() {
    return LAYOUT_INIT_SIZE;
  }

  public int getRefreshingHeight() {
    return REFRESH_HEIGHT;
  }

  public float getPullRate() {
    return mPullRate;
  }

  public abstract static class Builder {
    public float pullRate = 0.5f; // 下拉和实际移动比率
    public float pullOverRate = 0.5f;
    public int refreshFixableLayoutId = INVALID_LAYOUT_ID;
    public int animVelocity = 2000;
    public HeaderOffsetListener listener;

    public Builder() {}

    public Builder setPullRate(float rate) {
      this.pullRate = rate;
      return this;
    }

    public Builder setPullOverRate(float rate) {
      this.pullOverRate = rate;
      return this;
    }

    public Builder setRefreshFixableLayoutId(int id) {
      this.refreshFixableLayoutId = id;
      return this;
    }

    public Builder setAnimVelocity(int animVelocity) {
      this.animVelocity = animVelocity;
      return this;
    }

    public Builder setOffsetListener(HeaderOffsetListener listener) {
      this.listener = listener;
      return this;
    }

    public abstract RefreshLayout build(Context context);
  }
}
