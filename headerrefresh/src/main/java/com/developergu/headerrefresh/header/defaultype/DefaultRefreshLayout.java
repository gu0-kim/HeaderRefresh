package com.developergu.headerrefresh.header.defaultype;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.developergu.headerrefresh.HeaderRefreshRecyclerView;
import com.developergu.headerrefresh.R;
import com.developergu.headerrefresh.header.RefreshLayout;

/** Created by developgergu on 2018/1/4. */
public class DefaultRefreshLayout extends RefreshLayout {

  private ProgressArrowFrameLayout mProgressLayout;
  private TextView mText;

  public DefaultRefreshLayout(Context context) {
    this(context, null, new DefaultHeaderBuilder());
  }

  public DefaultRefreshLayout(Context context, @Nullable AttributeSet attrs) {
    this(context, attrs, null);
  }

  public DefaultRefreshLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
    this(context, attrs, null);
  }

  private DefaultRefreshLayout(Context context, @Nullable AttributeSet attrs, Builder builder) {
    super(context, attrs);
    if (builder != null) {
      this.mPullRate = builder.pullRate;
      this.mHeaderPullOverRate = builder.pullOverRate;
      this.mRefreshFixableLayoutId = builder.refreshFixableLayoutId;
      this.mAnimVelocity = builder.animVelocity;
      this.mOffsetListener = builder.listener;
    }
    init(context, attrs);
  }

  @Override
  protected void initRefreshLayout(Context context) {
    mRefreshEntityView =
        (ViewGroup) LayoutInflater.from(context).inflate(R.layout.refresh_layout, this, false);
    mProgressLayout = mRefreshEntityView.findViewById(R.id.ptr_arrow);
    mText = mRefreshEntityView.findViewById(R.id.ptr_tv);
  }

  @Override
  protected void configRefreshLayout() {
    mRefreshEntityView.setScrollY(MAX_PULL_SIZE);
    mProgressLayout.setRingMax(REFRESH_NEED_PULL_SIZE);
  }

  @Override
  protected void setStatePull() {
    mProgressLayout.direction(ProgressArrowFrameLayout.Direction.ARROW_DOWN);
    mText.setText(R.string.STR1);
  }

  @Override
  protected void setStateRelease2Refresh() {
    mProgressLayout.direction(ProgressArrowFrameLayout.Direction.ARROW_UP);
    mText.setText(R.string.STR2);
  }

  @Override
  protected void setStateRefreshing() {
    mProgressLayout.startRefresh();
    mText.setText(R.string.STR3);
  }

  @Override
  public void refreshing() {
    super.refreshing();
    setStateRefreshing();
  }

  @Override
  public void stopRefreshing() {
    super.stopRefreshing();
    mProgressLayout.stopRefresh();
    mText.setVisibility(View.INVISIBLE);
  }

  @Override
  public void forceStop() {
    super.forceStop();
    mProgressLayout.forceStop();
  }

  @Override
  protected void updateRefreshLayout(int newH, int deltaY) {
    super.updateRefreshLayout(newH, deltaY);
    mProgressLayout.setProgress(newH - LAYOUT_INIT_SIZE);
    mRefreshEntityView.scrollBy(0, deltaY);
  }

  @Override
  public void reset() {
    super.reset();
    mProgressLayout.setVisibility(View.VISIBLE);
    mText.setVisibility(View.VISIBLE);
    mProgressLayout.direction(ProgressArrowFrameLayout.Direction.ARROW_DOWN);
    mProgressLayout.setProgress(0);
    mState = HeaderRefreshRecyclerView.PullState.IDLE;
    mRefreshEntityView.scrollTo(0, MAX_PULL_SIZE);
  }

  @Override
  public void clear() {
    mState = null;
    mProgressLayout.clear();
  }

  public static class DefaultHeaderBuilder extends RefreshLayout.Builder {
    public DefaultHeaderBuilder() {
      super();
    }

    @Override
    public RefreshLayout build(Context context) {
      return new DefaultRefreshLayout(context, null, this);
    }
  }
}
