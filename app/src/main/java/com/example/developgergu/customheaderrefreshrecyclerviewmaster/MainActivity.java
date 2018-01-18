package com.example.developgergu.customheaderrefreshrecyclerviewmaster;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;

import com.example.developgergu.customheaderrefreshrecyclerviewmaster.model.IndexPageHttpModel;
import com.example.developgergu.customheaderrefreshrecyclerviewmaster.presenter.IndexPagePresenter;
import com.example.developgergu.customheaderrefreshrecyclerviewmaster.view.indexpage.ZhiFuBaoIndexView;

public class MainActivity extends AppCompatActivity {
  ZhiFuBaoIndexView fragment;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    fragment = (ZhiFuBaoIndexView) getSupportFragmentManager().findFragmentById(R.id.content_view);
    if (fragment == null) {
      fragment = ZhiFuBaoIndexView.getInstance();
      FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
      transaction.replace(R.id.content_view, fragment);
      transaction.commit();
    }
    fragment.setPresenter(new IndexPagePresenter(new IndexPageHttpModel()));
  }

  @Override
  public boolean dispatchTouchEvent(MotionEvent ev) {
    return !fragment.isCollapseAnimRunning() && super.dispatchTouchEvent(ev);
  }
}
