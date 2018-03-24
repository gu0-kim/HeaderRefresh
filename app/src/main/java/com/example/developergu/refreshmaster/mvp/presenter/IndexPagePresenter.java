package com.example.developergu.refreshmaster.mvp.presenter;

import android.util.Log;

import com.example.developergu.refreshmaster.mvp.model.IndexPageModel;
import com.example.developergu.refreshmaster.mvp.view.indexpage.ZhiFuBaoIndexView;
import com.gu.mvp.presenter.IPresenter;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;
import retrofit2.Response;

public class IndexPagePresenter extends IPresenter<ZhiFuBaoIndexView> {
  private IndexPageModel mModel;
  private static final String TAG = IndexPagePresenter.class.getSimpleName();

  public IndexPagePresenter(IndexPageModel model) {
    this.mModel = model;
  }

  public void loadDataFromServer() {
    addTask(
        mModel
            .getData()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                new Consumer<ResponseBody>() {
                  @Override
                  public void accept(ResponseBody responseBody) throws Exception {
                    String res = responseBody.string();
                    List<String> list = new ArrayList<>();
                    for (int i = 0; i < 10; i++) {
                      list.add(String.valueOf(i));
                    }
                    view.notifyLoadSuccess(list);
                    //
                  }
                },
                new Consumer<Throwable>() {
                  @Override
                  public void accept(Throwable throwable) throws Exception {
                    Log.e(TAG, "accept: " + throwable.getMessage());
                    List<String> list = new ArrayList<>();
                    for (int i = 0; i < 10; i++) {
                      list.add(String.valueOf(i));
                    }
                    view.notifyLoadSuccess(list);
                  }
                }));
  }
}
