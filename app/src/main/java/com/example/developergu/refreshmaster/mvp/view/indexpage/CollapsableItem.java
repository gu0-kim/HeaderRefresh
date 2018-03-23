package com.example.developergu.refreshmaster.mvp.view.indexpage;

import android.view.View;

/** Created by developergu on 2018/1/17. */
public interface CollapsableItem<T extends View> {

  /**
   * Judge whether needs to collapse.
   *
   * @param view current can be collapse
   * @return true if value in 0 ~ getTotalScrollRange
   */
  boolean isMiddleState(T view);

  void onCollapseScroll(int curScrollY);

  int getCurrentCollapse(T view);

  int getMaxCollapse();

  boolean hasEnoughSpace2Collapse(T view);

  boolean isCollapsed(int curScroll);

  float getCollapseRate(int curScroll);

  void startComebackAnim(T view);

  void scrollTo(int scrollY);

  int getNotShowHeight(T view);

  void setCurrentScrollY(int scrollY);
}
