package com.developergu.headerrefresh;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;

/** Created by developgergu on 2017/12/15. */
public class ScrollControlLinearLayoutManager extends LinearLayoutManager {
  private boolean canScroll = true;
  //    private boolean mFindHeader;

  public ScrollControlLinearLayoutManager(Context context) {
    super(context);
  }

  public ScrollControlLinearLayoutManager(Context context, int orientation, boolean reverseLayout) {
    super(context, orientation, reverseLayout);
  }

  public ScrollControlLinearLayoutManager(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
    super(context, attrs, defStyleAttr, defStyleRes);
  }

  @Override
  public boolean canScrollVertically() {
    return canScroll && super.canScrollVertically();
  }

  void setCanScroll(boolean canScroll) {
    this.canScroll = canScroll;
  }

  @Override
  public void onLayoutCompleted(RecyclerView.State state) {
    super.onLayoutCompleted(state);
    //        if (!mFindHeader && findFirstCompletelyVisibleItemPosition() == 0) {
    //            mFindHeader = true;
    //            RecyclerView recyclerView = (RecyclerView) findViewByPosition(0).getParent();
    //            if (recyclerView instanceof HeaderRefreshRecyclerView) {
    //                if (findFirstVisibleItemPosition() == 0) {
    //                    View view = findViewByPosition(0);
    //                    if (view instanceof HeaderLayout) {
    //                        float rate = ((HeaderLayout) view).getPullRate();
    //                        ((HeaderRefreshRecyclerView)
    // recyclerView).findHeaderPullRate(rate);
    //                        Log.e("TAG", "----onLayoutCompleted: " + rate);
    //                    }
    //                }
    //            }
    //        }
  }
}
