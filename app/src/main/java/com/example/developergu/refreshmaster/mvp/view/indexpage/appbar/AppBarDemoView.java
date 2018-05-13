package com.example.developergu.refreshmaster.mvp.view.indexpage.appbar;

import android.support.design.widget.AppBarLayout;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;

import com.developergu.headerrefresh.HeaderRefreshRecyclerView;
import com.developergu.headerrefresh.LlayoutManager;
import com.developergu.headerrefresh.header.RefreshLayout;
import com.developergu.headerrefresh.header.defaultype.DefaultRefreshLayout;
import com.example.developergu.refreshmaster.R;
import com.example.developergu.refreshmaster.di.component.ComponentController;
import com.example.developergu.refreshmaster.mvp.presenter.SimplePresenter;
import com.example.developergu.refreshmaster.mvp.view.indexpage.BottomDecoration;
import com.example.developergu.refreshmaster.mvp.view.indexpage.DataAdapter;
import com.gu.mvp.view.IView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/** Created by devel on 2018/5/13. */
public class AppBarDemoView extends IView<SimplePresenter>
    implements RefreshLayout.HeaderOffsetListener {
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
    List<String> data = new ArrayList<>();
    for (int i = 0; i < 20; i++) {
      data.add(String.valueOf(i));
    }
    mAdapter.add(data);
    mRecyclerView.setLayoutManager(new LlayoutManager(getContext()));
    mRecyclerView.setAdapter(mAdapter);
    mRecyclerView.addItemDecoration(new BottomDecoration(getContext(), 1));
    mRecyclerView.setAppBarLayout(appBar);
    //    mRecyclerView.setRefreshListener(this);
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
  }

  @Override
  public void onOffsetChanged(int offset, int refreshDimen, int maxOffset) {}
}
