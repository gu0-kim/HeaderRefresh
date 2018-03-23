package com.gu.mvp.view.adapter;

import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public abstract class IBaseAdapter<T, VH extends RecyclerView.ViewHolder>
    extends RecyclerView.Adapter<VH> {
  protected List<T> data;
  protected ItemClickListener mItemClickListener;

  public void setItemClickListener(ItemClickListener listener) {
    this.mItemClickListener = listener;
  }

  public interface ItemClickListener {
    void onItemClick(int pos);

    void onItemLongClick(int pos);
  }

  public IBaseAdapter() {
    super();
    data = new ArrayList<>();
  }

  @Override
  public int getItemCount() {
    if (data == null) return 0;
    return data.size();
  }

  public T getPositionData(int pos) {
    if (data == null) return null;
    return data.get(pos);
  }

  public void addAll(List<T> list) {
    if (data == null || list == null) return;
    if (!data.isEmpty()) data.clear();
    data.addAll(list);
  }

  public void clearAll() {
    if (data != null && !data.isEmpty()) data.clear();
    mItemClickListener = null;
  }
}
