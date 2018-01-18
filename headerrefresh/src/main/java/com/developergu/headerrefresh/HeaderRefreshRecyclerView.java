package com.developergu.headerrefresh;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.DecelerateInterpolator;

import com.developergu.headerrefresh.header.RefreshLayout;

import static android.support.v4.view.ViewCompat.TYPE_TOUCH;

/** Created by developgergu on 2017/12/14. */
public class HeaderRefreshRecyclerView extends RecyclerView {
  private AppBarLayout mAppBarLayout;
  private RefreshLayout mRefreshLayout;
  private View mCustomHeader;
  private ViewAnimController mAnimController;
  private int mTotalDeltaY, mLastTotalDeltaY;
  private float PULL_RATE;
  private RefreshListener mRefreshListener;
  private boolean canRefresh = true;
  private static final int INVALID_POINTER = -1;
  private int mLastMotionY, mActivePointerId;
  private static final String TAG = "TAG";
  private static final int MAX_VELOCITY_Y_ABS = 5000;

  public enum PullState {
    IDLE,
    PULL,
    RELEASE_TO_REFRESH,
    REFRESHING
  }

  public interface RefreshListener {
    void onRefreshStart();

    void onRefreshFinished();
  }

  public HeaderRefreshRecyclerView(Context context) {
    super(context);
    init();
  }

  public HeaderRefreshRecyclerView(Context context, @Nullable AttributeSet attrs) {
    super(context, attrs);
    init();
  }

  public HeaderRefreshRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
    super(context, attrs, defStyle);
    init();
  }

  public void setCanRefresh(boolean canRefresh) {
    this.canRefresh = canRefresh;
  }

  public void autoRefresh() {
    if (canRefresh)
      ViewCompat.postOnAnimationDelayed(
          this,
          new Runnable() {
            @Override
            public void run() {
              doRefresh();
            }
          },
          400);
  }

  private void init() {
    mAnimController = new ViewAnimController();
  }

  //  @Override
  //  protected void onScrollChanged(int l, int t, int oldl, int oldt) {
  //    super.onScrollChanged(l, t, oldl, oldt);
  //    Log.e("TAG", "******onScrollChanged: deltaY= " + (t - oldt) + ",t=" + t);
  //  }

  public void setRefreshListener(RefreshListener listener) {
    this.mRefreshListener = listener;
  }

  public void clearAllListener() {
    mRefreshListener = null;
    if (mRefreshLayout != null) {
      mRefreshLayout.setOffsetListener(null);
    }
  }

  private boolean forceMove;

  /**
   * call from outside to notify appbar to move
   *
   * @param deltaY dispatchPreScroll deltaY
   */
  public void moveAppBar(int deltaY) {
    forceMove = true;
    startNestedScroll(ViewCompat.SCROLL_AXIS_VERTICAL);
    if (deltaY > 0) dispatchNestedPreScroll(0, deltaY, null, null, TYPE_TOUCH);
    else if (deltaY < 0) {
      dispatchNestedScroll(0, 0, 0, deltaY, null);
    }
    forceMove = false;
  }

  /** bind the appbar */
  public void setAppBarLayout(AppBarLayout appBarLayout) {
    this.mAppBarLayout = appBarLayout;
  }

  private void setScrollable(boolean scrollable) {
    ((LlayoutManager) getLayoutManager()).setCanScroll(scrollable);
  }

  @Override
  public boolean dispatchNestedPreScroll(
      int dx, int dy, int[] consumed, int[] offsetInWindow, int type) {
    if (!forceMove && canRefresh && refreshLayoutExistAndVisible()) {
      setScrollable(false);
      return false;
    }
    setScrollable(true);
    return super.dispatchNestedPreScroll(dx, dy, consumed, offsetInWindow, type);
  }

  @Override
  public boolean dispatchTouchEvent(MotionEvent event) {
    if (event.getActionMasked() == MotionEvent.ACTION_DOWN) {
      mLastTotalDeltaY = mTotalDeltaY = 0;
      mLastMotionY = (int) event.getY();
      mActivePointerId = event.getPointerId(0);
    } else if (event.getActionMasked() == MotionEvent.ACTION_CANCEL
        || event.getActionMasked() == MotionEvent.ACTION_UP) {
      setScrollable(true);
    }
    // NOT call super.dispatchTouchEvent() when anim is continuing.
    return !isRefreshingOrAnimRunning() && super.dispatchTouchEvent(event);
  }

  @Override
  public boolean onTouchEvent(MotionEvent event) {
    switch (event.getActionMasked()) {
      case MotionEvent.ACTION_MOVE:
        final int activePointerIndex = event.findPointerIndex(mActivePointerId);
        if (activePointerIndex == INVALID_POINTER) {
          Log.e(TAG, "Invalid pointerId=" + mActivePointerId + " in onTouchEvent");
          break;
        }
        int y = (int) event.getY();
        if (refreshLayoutExist() && appBarOpen() && isTop()) {
          refreshLayoutMove(mLastMotionY - y);
        }
        mLastMotionY = y;
        break;
      case MotionEvent.ACTION_UP:
      case MotionEvent.ACTION_CANCEL:
        if (refreshLayoutExistAndVisible()) {
          mAnimController.prepareAnim(mRefreshLayout, mRefreshLayout.getCurrentState());
          ViewCompat.postOnAnimation(this, mAnimController);
        }
        break;
    }
    return super.onTouchEvent(event);
  }
  /**
   * Get the info whether it is starting to refresh.
   *
   * @return true if the recyclerView is startRefresh.
   */
  public boolean isRefreshingOrAnimRunning() {
    return mAnimController.isRunning()
        || mRefreshLayout != null && mRefreshLayout.getCurrentState() == PullState.REFRESHING;
  }

  public boolean fling(int velocityX, int velocityY) {
    return !refreshLayoutExistAndVisible()
        && super.fling(
            velocityX,
            (int) Math.signum(velocityY) * Math.min(Math.abs(velocityY), MAX_VELOCITY_Y_ABS));
  }

  private boolean appBarOpen() {
    return mAppBarLayout == null || mAppBarLayout.getTop() == 0;
  }

  private boolean isTop() {
    LinearLayoutManager linearLayoutManager = (LinearLayoutManager) getLayoutManager();
    return linearLayoutManager.findFirstCompletelyVisibleItemPosition() == 0;
  }

  public void refreshFinish() {
    PullState currentState = mRefreshLayout.getCurrentState();
    mRefreshLayout.stopRefreshing();
    mAnimController.prepareAnim(mRefreshLayout, currentState);
    ViewCompat.postOnAnimation(this, mAnimController);
  }

  public void refreshForceFinish() {
    if (mAnimController.isRunning()) {
      mAnimController.releaseAnim();
      if (mRefreshLayout != null) {
        mRefreshLayout.forceStop();
      }
    }
  }

  /** notify recyclerView to refresh. */
  public void doRefresh() {
    if (!refreshLayoutExistAndVisible() && mRefreshLayout != null) {
      mRefreshLayout.moveTo(mRefreshLayout.getRefreshingHeight());
      mRefreshLayout.refreshing();
      if (mRefreshListener != null) {
        mRefreshListener.onRefreshStart();
      }
    }
  }

  public void setRefreshLayoutHeaderView(RefreshLayout header) {
    this.mRefreshLayout = header;
    if (mRefreshLayout != null) {
      PULL_RATE = mRefreshLayout.getPullRate();
    }
  }

  public void setCustomHeaderView(View customHeader) {
    this.mCustomHeader = customHeader;
  }

  /**
   * use a wrapperAdapter to wrap the user adapter
   *
   * @param adapter User-defined adapters
   */
  @Override
  public void setAdapter(Adapter adapter) {
    super.setAdapter(new HeaderAndFooterWrapper(adapter, mCustomHeader, mRefreshLayout));
  }

  private void refreshLayoutMove(int deltaY) {
    if (deltaY != 0) {
      move(mRefreshLayout, deltaY);
    }
  }

  private void move(RefreshLayout refreshLayout, int deltaY) {
    mTotalDeltaY += deltaY;
    final int rateDeltaY = calcRateDeltaY();
    if (rateDeltaY != 0) {
      mLastTotalDeltaY = mTotalDeltaY;
      refreshLayout.moveBy(rateDeltaY);
    }
  }

  private int calcRateDeltaY() {
    return (int) ((mTotalDeltaY - mLastTotalDeltaY) * PULL_RATE);
  }

  /**
   * Judge whether RefreshLayout is visible
   *
   * @return true if RefreshLayout is visible
   */
  private boolean refreshLayoutExistAndVisible() {
    return refreshLayoutExist() && mRefreshLayout.pullVisible();
  }

  private boolean refreshLayoutExist() {
    return mRefreshLayout != null;
  }

  class ViewAnimController
      implements Runnable, ValueAnimator.AnimatorUpdateListener, ValueAnimator.AnimatorListener {
    ValueAnimator comebackAnim;
    RefreshLayout refreshLayout;
    PullState endState, startState;
    //    boolean running;

    ViewAnimController() {
      comebackAnim = new ValueAnimator();
      comebackAnim.addUpdateListener(this);
      comebackAnim.addListener(this);
      comebackAnim.setInterpolator(new DecelerateInterpolator());
    }

    void prepareAnim(RefreshLayout refreshLayout, PullState currentState) {
      this.refreshLayout = refreshLayout;
      if (currentState == PullState.PULL) {
        startState = PullState.PULL;
        endState = PullState.IDLE;
        configAnim(
            refreshLayout.getHeight(),
            refreshLayout.getLayoutInitSize(),
            refreshLayout.getAnimVelocity()); // 500
      } else if (currentState == PullState.RELEASE_TO_REFRESH) {
        startState = PullState.RELEASE_TO_REFRESH;
        endState = PullState.REFRESHING;
        configAnim(
            refreshLayout.getHeight(),
            refreshLayout.getRefreshingHeight(),
            refreshLayout.getAnimVelocity()); // 1000
      } else if (currentState == PullState.REFRESHING) {
        startState = PullState.REFRESHING;
        endState = PullState.IDLE;
        configAnim(
            refreshLayout.getRefreshingHeight(),
            refreshLayout.getLayoutInitSize(),
            refreshLayout.getAnimVelocity()); // 1000
      }
      //      running = false;
    }

    void startAnim() {
      //      running = true;
      comebackAnim.start();
    }

    private void configAnim(int from, int to, int vel) {
      comebackAnim.setIntValues(from, to);
      comebackAnim.setDuration(DimensUtils.calcDuration(from, to, vel));
    }

    void releaseAnim() {
      //      running = false;
      if (comebackAnim != null) {
        comebackAnim.cancel();
        comebackAnim.removeAllListeners();
        comebackAnim.removeAllUpdateListeners();
      }
      refreshLayout = null;
    }

    boolean isRunning() {
      return comebackAnim.isRunning();
    }

    @Override
    public void run() {
      startAnim();
    }

    @Override
    public void onAnimationUpdate(ValueAnimator animation) {
      if (refreshLayout != null) {
        refreshLayout.moveTo((Integer) animation.getAnimatedValue());
      }
    }

    @Override
    public void onAnimationStart(Animator animation) {}

    @Override
    public void onAnimationEnd(Animator animation) {
      if (refreshLayout == null) return;
      if (endState == PullState.IDLE) {
        refreshLayout.reset();
        if (startState == PullState.REFRESHING && mRefreshListener != null) {
          mRefreshListener.onRefreshFinished();
        }
        //        running = false;
      } else if (endState == PullState.REFRESHING) {
        refreshLayout.refreshing();
        if (mRefreshListener != null) {
          mRefreshListener.onRefreshStart();
        }
      }
      refreshLayout = null;
    }

    @Override
    public void onAnimationCancel(Animator animation) {}

    @Override
    public void onAnimationRepeat(Animator animation) {}
  }
}
