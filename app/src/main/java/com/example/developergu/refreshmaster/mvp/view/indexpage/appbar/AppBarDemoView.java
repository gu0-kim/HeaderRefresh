package com.example.developergu.refreshmaster.mvp.view.indexpage.appbar;

import android.support.design.widget.AppBarLayout;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import com.developergu.headerrefresh.HeaderRefreshRecyclerView;
import com.developergu.headerrefresh.ScrollControlLinearLayoutManager;
import com.developergu.headerrefresh.header.RefreshLayout;
import com.developergu.headerrefresh.header.defaultype.DefaultRefreshLayout;
import com.example.developergu.refreshmaster.R;
import com.example.developergu.refreshmaster.di.component.ComponentController;
import com.example.developergu.refreshmaster.mvp.presenter.SimplePresenter;
import com.example.developergu.refreshmaster.mvp.view.indexpage.BottomDecoration;
import com.example.developergu.refreshmaster.mvp.view.indexpage.DataAdapter;
import com.gu.mvp.utils.dimen.DimenUtils;
import com.gu.mvp.utils.scroll.ScrollUtils;
import com.gu.mvp.view.IView;
import com.gu.mvp.view.adapter.IBaseAdapter;
import com.jakewharton.rxbinding2.support.v7.widget.RxToolbar;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.reactivex.functions.Consumer;

/** Created by devel on 2018/5/13. */
public class AppBarDemoView extends IView<SimplePresenter>
    implements RefreshLayout.HeaderOffsetListener,
        HeaderRefreshRecyclerView.RefreshListener,
        IBaseAdapter.ItemClickListener {

  @BindView(R.id.header_rv)
  HeaderRefreshRecyclerView mRecyclerView;

  @BindView(R.id.toolbar)
  Toolbar mToolbar;

  @BindView(R.id.appBar)
  AppBarLayout appBar;

  private Unbinder mUnbinder;
  private DataAdapter mAdapter;

  public static AppBarDemoView newInstance() {
    return new AppBarDemoView();
  }

  @Override
  public boolean containsToolBar() {
    return true;
  }

  @Override
  public void inject() {
    ComponentController.getInstance().getComponent().inject(this);
  }

  @Override
  public int getLayoutId() {
    return R.layout.appbar_demo_layout;
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
        LayoutInflater.from(getContext()).inflate(R.layout.simple_header_layout, mToolbar, false);
    mRecyclerView.setCustomHeaderView(customHeader);
    mRecyclerView.setRefreshLayoutHeaderView(refreshLayout);
    mAdapter = new DataAdapter(getContext());
    mAdapter.setItemClickListener(this);
    mRecyclerView.setLayoutManager(new ScrollControlLinearLayoutManager(getContext()));
    mRecyclerView.setAdapter(mAdapter);
    mRecyclerView.addItemDecoration(new BottomDecoration(getContext(), 1));
    mRecyclerView.setAppBarLayout(appBar);
    mRecyclerView.setRefreshListener(this);
    mRecyclerView.autoRefresh();
    RxToolbar.navigationClicks(mToolbar)
        .subscribe(
            new Consumer<Object>() {
              @Override
              public void accept(Object o) throws Exception {
                if (getActivity() != null) getActivity().finish();
              }
            });
    mToolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp);
    if (DimenUtils.currentSdkUpVersion(14))
      ScrollUtils.addOnGlobalLayoutListener(
          mToolbar,
          new Runnable() {
            @Override
            public void run() {
              DimenUtils.setToolbarFitsSystemWindows(getContext(), mToolbar);
            }
          });
  }

  @Override
  public void destroyView() {
    mRecyclerView.clearAnim();
    mRecyclerView.clearAllListener();
    mRecyclerView.clearHeaderView();
    mAdapter.clearAll();
    mAdapter = null;
    mUnbinder.unbind();
    mUnbinder = null;
    if (tempList != null) {
      tempList.clear();
      tempList = null;
    }
  }

  @Override
  public void onOffsetChanged(int offset, int refreshDimen, int maxOffset) {}

  @Override
  public void onRefreshAnimStart() {
    presenter.loadSimpleData();
  }

  @Override
  public void onRefreshAnimFinished() {
    if (suc) {
      mAdapter.getList().addAll(tempList);
      mAdapter.notifyDataSetChanged();
      mRecyclerView.invalidateItemDecorations();
    }
  }

  private List<String> tempList;
  private boolean suc;

  public void notifyLoadFin(boolean suc, List<String> list) {
    this.suc = suc;
    if (suc && list != null) {
      if (tempList != null) tempList.clear();
      tempList = list;
    }
    // 先执行"结束动画"，再刷新数据
    mRecyclerView.doRefreshFinishAnim();
  }

  @Override
  public void onItemClick(int pos) {
    Log.e(getLogTagName(), "onItemClick: " + pos);
  }

  @Override
  public void onItemLongClick(int pos) {}
}
