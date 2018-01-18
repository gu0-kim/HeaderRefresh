package com.example.developgergu.customheaderrefreshrecyclerviewmaster;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.widget.Toast;

/** Created by developgergu on 2018/1/17. */
public class BaseFragment extends Fragment {

  protected void toastShow(Context context, String str) {
    Toast.makeText(context, str, Toast.LENGTH_LONG).show();
  }
}
