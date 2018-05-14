package com.example.developergu.refreshmaster;

import android.os.Bundle;
import android.view.MotionEvent;

import com.example.developergu.refreshmaster.mvp.view.indexpage.noappbar.ZhiFuBaoIndexView;

// first commit here!
public class ZhiFuBaoActivity extends BaseActivity {
  ZhiFuBaoIndexView currentFragment;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    currentFragment = getCurrentFragment();
    if (currentFragment == null) {
      showNewView();
    }
  }

  private void showNewView() {
    currentFragment = ZhiFuBaoIndexView.getInstance();
    getSupportFragmentManager()
        .beginTransaction()
        .replace(R.id.content_view, currentFragment)
        .commit();
  }

  private ZhiFuBaoIndexView getCurrentFragment() {
    return (ZhiFuBaoIndexView) getSupportFragmentManager().findFragmentById(R.id.content_view);
  }

  @Override
  public boolean dispatchTouchEvent(MotionEvent ev) {
    return currentFragment != null
        && !currentFragment.isCollapseAnimRunning()
        && super.dispatchTouchEvent(ev);
  }

  @Override
  protected void onDestroy() {
    super.onDestroy();
    checkItem(currentFragment);
    currentFragment = null;
  }
}
