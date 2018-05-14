package com.example.developergu.refreshmaster;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;

import com.example.developergu.refreshmaster.di.component.ComponentController;
import com.gu.mvp.utils.activity.StatusBarCompat;
import com.jakewharton.rxbinding2.support.v7.widget.RxToolbar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.functions.Consumer;

public class MainActivity extends BaseActivity {

  @BindView(R.id.toolbar)
  Toolbar toolbar;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    StatusBarCompat.compat(this, ContextCompat.getColor(this, R.color.colorPrimaryDark));
    setContentView(R.layout.main_list);
    ButterKnife.bind(this);
    RxToolbar.navigationClicks(toolbar)
        .subscribe(
            new Consumer<Object>() {
              @Override
              public void accept(Object o) throws Exception {
                finish();
              }
            });
  }

  @OnClick(R.id.appBarActivityBtn)
  public void startAppBarActivity() {
    startActivity(new Intent(this, SimpleDemoActivity.class));
  }

  @OnClick(R.id.zhifubaoActivityBtn)
  public void startZhiFuBaoActivity() {
    startActivity(new Intent(this, ZhiFuBaoActivity.class));
  }

  @Override
  protected void onDestroy() {
    super.onDestroy();
    ComponentController.getInstance().release();
  }
}
