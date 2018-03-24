package com.developergu.headerrefresh.header;

/** Created by developgergu on 2018/1/4. */
public interface RefreshableView {
  void refreshing();

  void stopRefreshing();

  void forceStop();

  void moveBy(int deltaY);

  void moveTo(int height);

  void reset();

  void clear();
}
