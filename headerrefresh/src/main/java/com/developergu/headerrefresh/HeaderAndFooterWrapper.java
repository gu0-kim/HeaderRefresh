package com.developergu.headerrefresh;

import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.Adapter;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

/** Created by developgergu on 2018/1/6. */
final class HeaderAndFooterWrapper extends Adapter {

  private View mRefreshHeaderView; // refresh item entity
  private View mCustomHeaderView; // user custom layout
  private View mFooterView;
  private Adapter mInnerAdapter;
  private boolean mHasRefreshHeader, mHasFooter, mHasCustomHeader;
  private int mFooterPos = -1;
  private int mRefreshHeaderPos = -1;
  private int mTotalHeaderCount;

  enum ItemType {
    HEADER_CUSTOM(-9),
    HEADER_REFRESH_LAYOUT(-10),
    FOOTER(-11);
    int type;

    ItemType(int type) {
      this.type = type;
    }
  }

  public HeaderAndFooterWrapper(Adapter innerAdapter) {
    this(innerAdapter, null, null);
  }

  HeaderAndFooterWrapper(Adapter innerAdapter, View customHeaderView, View refreshHeaderView) {
    this(innerAdapter, customHeaderView, refreshHeaderView, null);
  }

  private HeaderAndFooterWrapper(
      Adapter innerAdapter, View customHeaderView, View refreshHeaderView, View footerView) {
    super();
    mHasCustomHeader = (mCustomHeaderView = customHeaderView) != null;
    mHasRefreshHeader = (mRefreshHeaderView = refreshHeaderView) != null;
    mHasFooter = (mFooterView = footerView) != null;
    this.mInnerAdapter = innerAdapter;
    mFooterPos = mHasFooter ? getItemCount() - 1 : -1;
    mRefreshHeaderPos = mHasRefreshHeader ? (mHasCustomHeader ? 1 : 0) : -1;
    mTotalHeaderCount = totalHeaderCount();
  }

  @Override
  public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    if (viewType == ItemType.HEADER_CUSTOM.type) return new Holder(mCustomHeaderView);
    else if (viewType == ItemType.HEADER_REFRESH_LAYOUT.type) return new Holder(mRefreshHeaderView);
    else if (viewType == ItemType.FOOTER.type) return new Holder(mFooterView);
    else if (mInnerAdapter == null) return null;
    else {
      RecyclerView.ViewHolder holder = mInnerAdapter.onCreateViewHolder(parent, viewType);
      /*
       * -----如果itemView没有消耗事件，recyclerView的onInterceptTouchEvent不会拦截事件，
       * 不会执行setScrollState(SCROLL_STATE_DRAGGING)，起始状态就不是SCROLL_STATE_DRAGGING，而是SCROLL_STATE_IDLE
       * 前台activity的addOnScrollListener向上滚动时就不会收到onScrollStateChanged
       * （actionUp时默认是SCROLL_STATE_IDLE,因为状态没变一直是SCROLL_STATE_IDLE）。 -----
       * 因此需要保证每一个item都消耗事件，便于前台统一处理
       */
      if (!holder.itemView.hasOnClickListeners()) addTouchListener(holder.itemView);
      return holder;
    }
  }

  @Override
  public int getItemViewType(int position) {
    return isCustomHeaderPos(position)
        ? ItemType.HEADER_CUSTOM.type
        : isRefreshHeaderPos(position)
            ? ItemType.HEADER_REFRESH_LAYOUT.type
            : isFooterPos(position)
                ? ItemType.FOOTER.type
                : mInnerAdapter.getItemViewType(position - mTotalHeaderCount);
  }

  @SuppressWarnings("unchecked")
  @Override
  public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
    Log.e("TAG", "onBindViewHolder: position= " + position);
    if (isFooterPos(position) || isCustomHeaderPos(position) || isRefreshHeaderPos(position))
      return;
    if (mInnerAdapter != null) {
      mInnerAdapter.onBindViewHolder(holder, position - mTotalHeaderCount);
    }
  }

  @Override
  public int getItemCount() {
    return mInnerAdapter == null
        ? headerAndFooterCount()
        : mInnerAdapter.getItemCount() + headerAndFooterCount();
  }

  public void release() {
    mRefreshHeaderView = null;
    mCustomHeaderView = null;
    mFooterView = null;
    mInnerAdapter = null;
  }

  private int headerAndFooterCount() {
    return (mHasFooter ? 1 : 0) + totalHeaderCount();
  }

  private boolean isRefreshHeaderPos(int pos) {
    return mHasRefreshHeader && pos == mRefreshHeaderPos;
  }

  private boolean isCustomHeaderPos(int pos) {
    return mHasCustomHeader && pos == 0;
  }

  private boolean isFooterPos(int pos) {
    return mHasFooter && pos == mFooterPos;
  }

  private int getOffsetHeaderPos() {
    return mTotalHeaderCount;
  }

  private int totalHeaderCount() {
    return (mHasCustomHeader ? 1 : 0) + (mHasRefreshHeader ? 1 : 0);
  }

  static class Holder extends RecyclerView.ViewHolder {
    Holder(View itemView) {
      super(itemView);
      itemView.setOnTouchListener(
          new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
              return true;
            }
          });
    }
  }

  private void addTouchListener(View view) {
    Log.e("TAG", "------addTouchListener: --------");
    view.setOnTouchListener(
        new View.OnTouchListener() {
          @Override
          public boolean onTouch(View v, MotionEvent event) {
            return true;
          }
        });
  }
}
