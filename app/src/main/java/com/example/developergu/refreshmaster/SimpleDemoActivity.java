package com.example.developergu.refreshmaster;

import android.os.Build;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import com.example.developergu.refreshmaster.mvp.view.indexpage.appbar.AppBarDemoView;

/** Created by devel on 2018/5/13. */
public class SimpleDemoActivity extends BaseActivity {
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    showAppBarDemonView();
    //    currentFragment = getCurrentFragment();
    //    if (currentFragment == null) {
    //      showNewView();
    //    }
    //    initWindow();
  }

  private void showAppBarDemonView() {
    getSupportFragmentManager()
        .beginTransaction()
        .replace(R.id.content_view, AppBarDemoView.newInstance())
        .commit();
  }

  private void initWindow() {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
      final Window window = getWindow();
      window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
      window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
    }
  }
}
