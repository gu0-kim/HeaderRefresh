package com.example.developergu.refreshmaster;

import android.os.Bundle;

import com.example.developergu.refreshmaster.mvp.view.indexpage.appbar.AppBarDemoView;

/** Created by devel on 2018/5/13. */
public class SimpleDemoActivity extends BaseActivity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    showAppBarDemonView();

    //    ViewGroup contentFrameLayout = (ViewGroup) findViewById(Window.ID_ANDROID_CONTENT);
    //    View parentView = contentFrameLayout.getChildAt(0);
    //    if (parentView != null && Build.VERSION.SDK_INT >= 14) {
    //      parentView.setFitsSystemWindows(true);
    //    }
  }

  private void showAppBarDemonView() {
    getSupportFragmentManager()
        .beginTransaction()
        .replace(R.id.content_view, AppBarDemoView.newInstance())
        .commit();
  }
}
