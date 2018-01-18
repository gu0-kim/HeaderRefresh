package com.example.developgergu.customheaderrefreshrecyclerviewmaster.presenter;

import com.example.developgergu.customheaderrefreshrecyclerviewmaster.model.EntityResult;
import com.example.developgergu.customheaderrefreshrecyclerviewmaster.model.IndexPageHttpModel;
import com.example.developgergu.customheaderrefreshrecyclerviewmaster.view.indexpage.ZhiFuBaoIndexView;

import java.util.List;

import io.reactivex.functions.Consumer;

/** Created by developgergu on 2018/1/18. */
public class IndexPagePresenter extends IPresenter<ZhiFuBaoIndexView, IndexPageHttpModel> {

  public IndexPagePresenter(IndexPageHttpModel model) {
    super();
    this.model = model;
  }

  public void loadDataFromServer() {
    mCompositeDisposable.add(
        model
            .createTask()
            .load(
                new Consumer<EntityResult<List<String>>>() {
                  @Override
                  public void accept(EntityResult<List<String>> result) throws Exception {
                    if (result.isSuccessful()) {
                      view.notifyLoadSuccess(result.getData());
                    } else {
                      view.notifyLoadFail();
                    }
                  }
                }));
  }
}
