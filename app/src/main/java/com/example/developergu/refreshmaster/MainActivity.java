package com.example.developergu.refreshmaster;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;

import com.example.developergu.refreshmaster.view.indexpage.ZhiFuBaoIndexView;
// first commit here!
public class MainActivity extends AppCompatActivity {
  ZhiFuBaoIndexView currentFragment;
  //  static MyApplication mApplication;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    currentFragment = getCurrentFragment();
    if (currentFragment == null) {
      showNewView();
    }
    //    mApplication = (MyApplication) getApplication();
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
    CleanLeakUtils.fixInputMethodManagerLeak(this);
    checkItem(currentFragment);
    checkItem(this);
    currentFragment = null;
  }

  public void checkItem(Object object) {
    if (object != null) {
      Log.e("TAG", "checkItem: " + object);
      ((MyApplication) getApplication()).getRefWatcher().watch(object);
    }
  }
}
