package com.example.developergu.refreshmaster;

import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.example.developergu.refreshmaster.app.MyApplication;
import com.gu.mvp.utils.leaks.CleanLeakUtils;

/** Created by devel on 2018/5/14. */
public abstract class BaseActivity extends AppCompatActivity {

  @Override
  protected void onDestroy() {
    super.onDestroy();
    CleanLeakUtils.fixInputMethodManagerLeak(this);
    checkItem(this);
  }

  public void checkItem(Object object) {
    if (object != null) {
      Log.e("TAG", "checkItem: " + object);
      ((MyApplication) getApplication()).getRefWatcher().watch(object);
    }
  }
}
