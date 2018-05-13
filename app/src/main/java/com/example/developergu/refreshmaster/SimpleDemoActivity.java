package com.example.developergu.refreshmaster;

import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;

import com.example.developergu.refreshmaster.mvp.view.indexpage.appbar.AppBarDemoView;
import com.gu.mvp.utils.activity.StatusBarCompat;

/** Created by devel on 2018/5/13. */
public class SimpleDemoActivity extends AppCompatActivity {
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    showAppBarDemonView();
    //    currentFragment = getCurrentFragment();
    //    if (currentFragment == null) {
    //      showNewView();
    //    }
    StatusBarCompat.compat(this, ContextCompat.getColor(this, R.color.colorPrimaryDark));
  }

  private void showAppBarDemonView() {
    getSupportFragmentManager()
        .beginTransaction()
        .replace(R.id.content_view, AppBarDemoView.newInstance())
        .commit();
  }
}
