package com.gu.mvp.view.fragment;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Toast;

public abstract class BaseBottomSheetDialogFragment extends BottomSheetDialogFragment
    implements IFragment {

  protected Activity mActivity;

  @Override
  public String getLogTagName() {
    return this.getClass().getSimpleName();
  }

  @Override
  public void onAttach(Context context) {
    super.onAttach(context);
    mActivity = (Activity) context;
    Log.e(getLogTagName(), getLogTagName() + "--onAttach: ");
  }

  @Override
  public void onDetach() {
    super.onDetach();
    mActivity = null;
    Log.e(getLogTagName(), getLogTagName() + "--onDetach: ");
  }

  @Override
  public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    Log.e(getLogTagName(), getLogTagName() + "--onCreate: ");
    //    setStyle(STYLE_NO_TITLE, R.style.bottom_sheet_dialog_style);
  }

  @Override
  public Dialog onCreateDialog(Bundle savedInstanceState) {
    Dialog dialog = super.onCreateDialog(savedInstanceState);
    backgroundTransparent(dialog);
    return dialog;
  }

  @Nullable
  @Override
  public View onCreateView(
      @NonNull LayoutInflater inflater,
      @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    View parent = inflater.inflate(getLayoutId(), container, false);
    initView(parent);
    Log.e(getLogTagName(), getLogTagName() + "--onCreateView: ");
    return parent;
  }

  @Override
  public void onResume() {
    super.onResume();
    Log.e(getLogTagName(), getLogTagName() + "--onResume: ");
  }

  @Override
  public void onPause() {
    super.onPause();
    Log.e(getLogTagName(), getLogTagName() + "--onPause: ");
  }

  @Override
  public void onStop() {
    super.onStop();
    Log.e(getLogTagName(), getLogTagName() + "--onStop: ");
  }

  @Override
  public void onDestroyView() {
    super.onDestroyView();
    destroyView();
    Log.e(getLogTagName(), getLogTagName() + "--onDestroyView: ");
  }

  @Override
  public void onDestroy() {
    super.onDestroy();
    Log.e(getLogTagName(), getLogTagName() + "--onDestroy: ");
  }

  @Override
  public void showToast(Context context, String toast) {
    Toast.makeText(context, toast, Toast.LENGTH_SHORT).show();
  }

  public String getStringByKey(String key) {
    Bundle data = getArguments();
    if (data != null) return data.getString(key);
    return "";
  }

  //  public int getIntByKey(String key) {
  //    Bundle data = getArguments();
  //    if (data != null) return data.getInt(key);
  //    return -1;
  //  }

  public long getLongByKey(String key) {
    Bundle data = getArguments();
    if (data != null) return data.getLong(key);
    return -1;
  }

  public void saveLongByKey(String key, long value) {
    Bundle data = getArguments();
    if (data == null) return;
    data.putLong(key, value);
  }

  //  public void saveIntByKey(String key, int value) {
  //    Bundle data = getArguments();
  //    if (data == null) return;
  //    data.putInt(key, value);
  //  }

  public void saveStringByKey(String key, String value) {
    Bundle data = getArguments();
    if (data == null) return;
    data.putString(key, value);
  }

  private void backgroundTransparent(Dialog dialog) {
    Window window = dialog.getWindow();
    if (window != null) {
      Log.e("tag", "----backgroundTransparent: set background!----");
      window.setDimAmount(0.0f);
    }
  }
}
