package com.example.developergu.refreshmaster.mvp.presenter;

import com.example.developergu.refreshmaster.mvp.view.indexpage.appbar.AppBarDemoView;
import com.gu.mvp.presenter.IPresenter;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;

/** Created by devel on 2018/5/13. */
public class SimplePresenter extends IPresenter<AppBarDemoView> {
  public void loadSimpleData() {
    addTask(
        Observable.timer(4, TimeUnit.SECONDS)
            .take(1)
            .map(
                new Function<Long, List<String>>() {
                  @Override
                  public List<String> apply(Long arg) throws Exception {
                    List<String> data = new ArrayList<>();
                    for (int i = 0; i < 5; i++) {
                      data.add(String.valueOf(i));
                    }
                    return data;
                  }
                })
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                new Consumer<List<String>>() {
                  @Override
                  public void accept(List<String> data) throws Exception {
                    view.notifyLoadFin(true, data);
                  }
                }));
  }
}
