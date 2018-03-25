package com.example.developergu.refreshmaster.mvp.view.indexpage;

import android.support.annotation.NonNull;
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
import com.example.developergu.refreshmaster.R;
import com.example.developergu.refreshmaster.app.MyApplication;
import com.example.developergu.refreshmaster.di.component.DaggerActivityComponent;
import com.example.developergu.refreshmaster.mvp.presenter.IndexPagePresenter;
import com.gu.mvp.utils.scroll.ScrollUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

import static android.support.v7.widget.RecyclerView.SCROLL_STATE_IDLE;

/** Created by developergu on 2018/1/17. */
public class ZhiFuBaoIndexView
    extends HeaderCollapseView<HeaderRefreshRecyclerView, IndexPagePresenter>
    implements RefreshLayout.HeaderOffsetListener, HeaderRefreshRecyclerView.RefreshListener {

  @BindView(R.id.toolbar)
  Toolbar mToolbar;

  @BindView(R.id.fold_layout)
  RelativeLayout fold_layout;

  @BindView(R.id.open_layout)
  RelativeLayout open_layout;

  @BindView(R.id.toolbar_below)
  LinearLayout mCollapseParallaxLayout;

  @BindView(R.id.header_rv)
  HeaderRefreshRecyclerView mRecyclerView;

  private LinearLayout front_mask_view;
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

  private Unbinder mUnbinder;

  @NonNull
  public static ZhiFuBaoIndexView getInstance() {
    return new ZhiFuBaoIndexView();
  }

  @Override
  public void injectPresenter() {
    if (getActivity() == null) return;
    MyApplication application = (MyApplication) getActivity().getApplication();
    DaggerActivityComponent.builder()
        .appComponent(application.getAppComponent())
        .build()
        .inject(this);
  }

  @Override
  public int getLayoutId() {
    return R.layout.fragmen_layout;
  }

  @Override
  public void initView(View parent) {
    mUnbinder = ButterKnife.bind(this, parent);
    RefreshLayout refreshLayout =
        new DefaultRefreshLayout.DefaultHeaderBuilder()
            //            .setRefreshFixableLayoutId(R.layout.header_btn_layout)
            .setPullRate(0.5f)
            .setPullOverRate(0.5f)
            .setOffsetListener(this)
            .setAnimVelocity(300)
            .build(getContext());
    View customHeader =
        LayoutInflater.from(getContext()).inflate(R.layout.header_custom_layout, mToolbar, false);
    front_mask_view = customHeader.findViewById(R.id.top_front);
    mRecyclerView.setCustomHeaderView(customHeader);
    mRecyclerView.setRefreshLayoutHeaderView(refreshLayout);
    data = new ArrayList<>();
    mAdapter = new DataAdapter(getContext(), data);
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
  public void destroyView() {
    mRecyclerView.clearAnim();
    mRecyclerView.clearAllListener();
    mRecyclerView.clearHeaderView();
    mRecyclerView = null;
    mAdapter.clear();
    if (temp != null) {
      temp.clear();
      temp = null;
    }
    mUnbinder.unbind();
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
    mCollapseParallaxLayout.setTranslationY(calcParallaxViewTransY(curScrollY));
    float rate = Math.abs(getCollapseRate(curScrollY));
    if (rate <= COLLAPSE_RATE_TRIGGER) {
      fold_layout.setVisibility(View.VISIBLE);
      fold_layout.setAlpha(1f - rate / COLLAPSE_RATE_TRIGGER);
    } else {
      fold_layout.setVisibility(View.GONE);
      open_layout.setVisibility(View.VISIBLE);
      open_layout.setAlpha(Math.min(rate - COLLAPSE_RATE_TRIGGER + MIN_ALPHA, 1f));
      //      front_mask_view.setAlpha(Math.min(rate - COLLAPSE_RATE_TRIGGER, 1f -
      // MIN_ALPHA));
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

  private int calcParallaxViewTransY(int scrollY) {
    return Math.max(maxTop - (int) (scrollY * COLLAPSE_PARALLAX_MULTIPLIER), minTop);
  }

  @Override
  public void onRefreshAnimStart() {
    presenter.loadDataFromServer();
  }

  boolean suc;

  @Override
  public void onRefreshAnimFinished() {
    // 动画结束后，更新数据
    if (suc) {
      data.addAll(temp);
      mAdapter.notifyDataSetChanged();
      mRecyclerView.invalidateItemDecorations();
    } else {
      showToast(getContext(), "加载失败");
    }
    if (temp != null) temp.clear();
  }

  List<String> temp;

  public void notifyLoadSuccess(List<String> list) {
    // 执行结束动画
    suc = true;
    temp = list;
    mRecyclerView.doRefreshFinishAnim();
  }

  public void notifyLoadFail() {
    suc = false;
    mRecyclerView.doRefreshFinishAnim();
  }
}