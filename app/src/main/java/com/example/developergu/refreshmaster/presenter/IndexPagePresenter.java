package com.example.developergu.refreshmaster.presenter;

import android.util.Log;

import com.example.developergu.refreshmaster.model.ModelResult;
import com.example.developergu.refreshmaster.model.indexpage.IndexPageLocalModel;
import com.example.developergu.refreshmaster.view.indexpage.ZhiFuBaoIndexView;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;

/** Created by developergu on 2018/1/18. */
public class IndexPagePresenter extends IPresenter<ZhiFuBaoIndexView, List<String>> {

  public IndexPagePresenter() {
    super();
  }

  @Override
  public Observable<ModelResult<List<String>>> getModeTask() {
    return new IndexPageLocalModel().createModeTask();
  }

  public void loadDataFromServer() {
    startModeTask(
        getModeTask(),
        new Consumer<ModelResult<List<String>>>() {
          @Override
          public void accept(ModelResult<List<String>> result) throws Exception {
            if (result.isSuccessful()) {
              getView().notifyLoadSuccess(result.getData());
            } else {
              Log.e(
                  "TAG",
                  "fail,errorCode="
                      + result.getErrorCode()
                      + ",errorMessage="
                      + result.getErrorMessage());
              getView().notifyLoadFail();
            }
          }
        },
        new Consumer<Throwable>() {
          @Override
          public void accept(Throwable throwable) throws Exception {
            Log.e("TAG", "accept: throwable is " + throwable.getMessage());
            getView().notifyLoadFail();
          }
        },
        new Action() {
          @Override
          public void run() throws Exception {
            // here do nothing!
          }
        });
  }
}
