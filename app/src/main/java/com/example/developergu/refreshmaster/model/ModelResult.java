package com.example.developergu.refreshmaster.model;

/** Created by developergu on 2018/1/17. */
// 统一返回结果
public class ModelResult<T> {
  private T data;
  private boolean isSuccessful;
  private int errorCode;
  private String errorMessage;

  public ModelResult(T data) {
    if (data != null) {
      this.data = data;
      isSuccessful = true;
    } else {
      isSuccessful = false;
      errorMessage = "data is NULL";
    }
  }

  public ModelResult(int errorCode, String errorMessage) {
    this.errorCode = errorCode;
    this.isSuccessful = false;
    this.errorMessage = errorMessage;
  }

  public boolean isSuccessful() {
    return isSuccessful;
  }

  public int getErrorCode() {
    return errorCode;
  }

  public String getErrorMessage() {
    return errorMessage;
  }

  public T getData() {
    return data;
  }
}
