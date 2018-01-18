package com.example.developgergu.customheaderrefreshrecyclerviewmaster.view.indexpage;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.developgergu.customheaderrefreshrecyclerviewmaster.R;

import java.util.List;

/** Created by developgergu on 2017/12/20. */
public class DataAdapter extends RecyclerView.Adapter<DataAdapter.ViewHolder> {

  private Context mContext;
  private List<String> data;

  DataAdapter(Context context, List<String> data) {
    mContext = context;
    this.data = data;
  }

  @Override
  public DataAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    return new ViewHolder(LayoutInflater.from(mContext).inflate(R.layout.list_item, parent, false));
  }

  @Override
  public void onBindViewHolder(DataAdapter.ViewHolder holder, int position) {
    ((TextView) holder.itemView).setText(data.get(position));
  }

  @Override
  public int getItemCount() {
    return data == null ? 0 : data.size();
  }

  public void clear() {
    if (data != null && !data.isEmpty()) {
      data.clear();
      data = null;
    }
  }

  static class ViewHolder extends RecyclerView.ViewHolder {

    ViewHolder(View itemView) {
      super(itemView);
    }
  }
}
