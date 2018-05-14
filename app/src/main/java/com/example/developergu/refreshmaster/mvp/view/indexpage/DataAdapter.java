package com.example.developergu.refreshmaster.mvp.view.indexpage;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.developergu.refreshmaster.R;
import com.example.developergu.refreshmaster.mvp.view.indexpage.DataAdapter.ViewHolder;
import com.gu.mvp.view.adapter.IBaseAdapter;

/** Created by developergu on 2017/12/20. */
public class DataAdapter extends IBaseAdapter<String, ViewHolder> {

  private Context mContext;

  public DataAdapter(Context context) {
    super();
    mContext = context;
  }

  @NonNull
  @Override
  public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    return new ViewHolder(LayoutInflater.from(mContext).inflate(R.layout.list_item, parent, false));
  }

  @Override
  public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
    ((TextView) holder.itemView).setText(getPositionData(position));
  }

  class ViewHolder extends RecyclerView.ViewHolder {

    ViewHolder(View itemView) {
      super(itemView);
      itemView.setOnClickListener(
          new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              if (mItemClickListener != null) {
                mItemClickListener.onItemClick(getAdapterPosition());
              }
            }
          });
    }
  }
}
