package com.example.developgergu.customheaderrefreshrecyclerviewmaster.view.indexpage;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.developergu.headerrefresh.HeaderRefreshRecyclerView;
import com.developergu.headerrefresh.LlayoutManager;
import com.developergu.headerrefresh.header.RefreshLayout;
import com.developergu.headerrefresh.header.defaultype.DefaultRefreshLayout;
import com.example.developgergu.customheaderrefreshrecyclerviewmaster.R;
import com.example.developgergu.customheaderrefreshrecyclerviewmaster.presenter.IndexPagePresenter;
import com.example.developgergu.customheaderrefreshrecyclerviewmaster.utils.ScrollUtils;

import java.util.ArrayList;
import java.util.List;

import static android.support.v7.widget.RecyclerView.SCROLL_STATE_IDLE;

/** Created by developgergu on 2018/1/17. */
public class ZhiFuBaoIndexView
    extends HeaderCollapseView<HeaderRefreshRecyclerView, IndexPagePresenter>
    implements RefreshLayout.HeaderOffsetListener, HeaderRefreshRecyclerView.RefreshListener {
  private Toolbar mToolbar;
  private RelativeLayout fold_layout, open_layout;
  private LinearLayout mCollapseParallaxLayout;
  private LinearLayout front_mask_view;
  private HeaderRefreshRecyclerView mRecyclerView;
  private DataAdapter mAdapter;
  private List<String> data;

  private int mParallaxViewHeight, mToolbarHeight;
  private int minTop, maxTop;
  private boolean initTranslation;
  private int mCurrentScrollY;
  private static final float COLLAPSE_PARALLAX_MULTIPLIER = 0.6f; // 异步滚动因子
  private static final float COLLAPSE_RATE_TRIGGER = 0.2f; // 折叠到40%开始切换toolbar
  private static final float MIN_ALPHA = 0.3f;
  private static final String TAG = ZhiFuBaoIndexView.class.getName();

  public static ZhiFuBaoIndexView getInstance() {
    return new ZhiFuBaoIndexView();
  }

  // call from activity.
  @Override
  public void setPresenter(IndexPagePresenter presenter) {
    mPresenter = presenter;
    mPresenter.bindView(this);
  }

  @Override
  public void initView(Context context, View parent, LayoutInflater inflater) {
    mToolbar = parent.findViewById(R.id.toolbar);
    fold_layout = parent.findViewById(R.id.fold_layout);
    open_layout = parent.findViewById(R.id.open_layout);
    mRecyclerView = parent.findViewById(R.id.header_rv);
    mCollapseParallaxLayout = parent.findViewById(R.id.toolbar_below);
    RefreshLayout refreshLayout =
        new DefaultRefreshLayout.DefaultHeaderBuilder()
            //            .setRefreshFixableLayoutId(R.layout.header_btn_layout)
            .setPullRate(0.5f)
            .setPullOverRate(0.5f)
            .setOffsetListener(this)
            .setAnimVelocity(300)
            .build(context);
    View customHeader = inflater.inflate(R.layout.header_custom_layout, mToolbar, false);
    front_mask_view = customHeader.findViewById(R.id.top_front);
    mRecyclerView.setCustomHeaderView(customHeader);
    mRecyclerView.setRefreshLayoutHeaderView(refreshLayout);
    data = new ArrayList<>();
    for (int i = 0; i < 1; i++) {
      data.add(String.valueOf(i));
    }
    mAdapter = new DataAdapter(context, data);
    mRecyclerView.setLayoutManager(new LlayoutManager(getContext()));
    mRecyclerView.setAdapter(mAdapter);
    mRecyclerView.addItemDecoration(new BottomDecoration(getContext(), 1));
    mRecyclerView.setRefreshListener(this);
    mRecyclerView.addOnScrollListener(
        new RecyclerView.OnScrollListener() {
          @Override
          public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);
            if (newState == SCROLL_STATE_IDLE
                && isMiddleState((HeaderRefreshRecyclerView) recyclerView)) {
              startComebackAnim((HeaderRefreshRecyclerView) recyclerView);
            }
          }

          @Override
          public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
            mCurrentScrollY += dy;
            if (initTranslation) onCollapseScroll(mCurrentScrollY);
          }
        });
    ScrollUtils.addOnGlobalLayoutListener(
        mCollapseParallaxLayout,
        new Runnable() {
          @Override
          public void run() {
            mParallaxViewHeight = mCollapseParallaxLayout.getHeight();
            mToolbarHeight = mToolbar.getHeight();
            minTop = mToolbarHeight - (int) (mParallaxViewHeight * COLLAPSE_PARALLAX_MULTIPLIER);
            maxTop = mToolbarHeight;
            mCollapseParallaxLayout.setTranslationY(maxTop);
            front_mask_view.setAlpha(0);
            initTranslation = true;
          }
        });
  }

  @Override
  public void clear() {
    if (mRecyclerView.isRefreshingOrAnimRunning()) mRecyclerView.refreshForceFinish();
    mRecyclerView.clearAllListener();
    mAdapter.clear();
    if (temp != null) {
      temp.clear();
      temp = null;
    }
  }

  @Override
  public boolean isMiddleState(HeaderRefreshRecyclerView view) {
    return mCurrentScrollY < mParallaxViewHeight && mCurrentScrollY != 0;
  }

  private int mLastCollapseScrollY;

  @Override
  public void onCollapseScroll(int curScrollY) {
    if (curScrollY > mParallaxViewHeight) {
      if (mLastCollapseScrollY == mParallaxViewHeight) return;
      curScrollY = mParallaxViewHeight;
    }
    mLastCollapseScrollY = curScrollY;
    mCollapseParallaxLayout.setTranslationY(parallaxViewTransY(curScrollY));
    float rate = Math.abs(getCollapseRate(curScrollY));
    if (rate <= COLLAPSE_RATE_TRIGGER) {
      fold_layout.setVisibility(View.VISIBLE);
      fold_layout.setAlpha(1f - rate / COLLAPSE_RATE_TRIGGER);
    } else {
      fold_layout.setVisibility(View.GONE);
      open_layout.setVisibility(View.VISIBLE);
      open_layout.setAlpha(Math.min(rate - COLLAPSE_RATE_TRIGGER + MIN_ALPHA, 1f));
      //      front_mask_view.setAlpha(Math.min(rate - COLLAPSE_RATE_TRIGGER, 1f - MIN_ALPHA));
    }
    front_mask_view.setAlpha(rate);
  }

  @Override
  public int getCurrentCollapse(HeaderRefreshRecyclerView view) {
    return mCurrentScrollY;
  }

  @Override
  public int getMaxCollapse() {
    return mParallaxViewHeight;
  }

  @Override
  public void setCurrentScrollY(int scrollY) {
    mCurrentScrollY = scrollY;
  }

  @Override
  public boolean hasEnoughSpace2Collapse(HeaderRefreshRecyclerView view) {
    return getNotShowHeight(view) > mParallaxViewHeight - mCurrentScrollY;
  }

  @Override
  public int getNotShowHeight(HeaderRefreshRecyclerView view) {
    return view.computeVerticalScrollRange()
        - view.computeVerticalScrollExtent()
        - view.computeVerticalScrollOffset();
  }

  @Override
  public void startComebackAnim(HeaderRefreshRecyclerView view) {
    mCollapseAnimController.ifStarted2Cancel();
    boolean collapse = isCollapsed(getCurrentCollapse(view));
    if (collapse && hasEnoughSpace2Collapse(view)) {
      mCollapseAnimController.setCollapseAnimValues(getCurrentCollapse(view), getMaxCollapse());
    } else {
      mCollapseAnimController.setCollapseAnimValues(getCurrentCollapse(view), 0);
    }
    mCollapseAnimController.startCollapseAnim();
  }

  @Override
  public void scrollTo(int scrollY) {
    ((LinearLayoutManager) mRecyclerView.getLayoutManager())
        .scrollToPositionWithOffset(0, -scrollY);
  }

  @Override
  public void onOffsetChanged(int offset, int refreshDimen, int maxOffset) {}

  private int parallaxViewTransY(int scrollY) {
    return Math.max(maxTop - (int) (scrollY * COLLAPSE_PARALLAX_MULTIPLIER), minTop);
  }

  @Override
  public void onRefreshStart() {
    mPresenter.loadDataFromServer();
  }

  boolean suc;

  @Override
  public void onRefreshFinished() {
    // 动画结束后，更新数据
    if (suc) {
      data.addAll(temp);
      mAdapter.notifyDataSetChanged();
      mRecyclerView.invalidateItemDecorations();
    } else {
      toastShow(getContext(), "加载失败");
    }
  }

  List<String> temp;

  public void notifyLoadSuccess(List<String> list) {
    // 执行结束动画
    suc = true;
    if (temp != null) temp.clear();
    temp = list;
    mRecyclerView.refreshFinish();
  }

  public void notifyLoadFail() {
    suc = false;
    mRecyclerView.refreshFinish();
  }
}
