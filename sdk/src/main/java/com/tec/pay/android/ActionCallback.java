package com.tec.pay.android;

public interface ActionCallback {

  void onSucceed();

  void onFailed(Exception e);
}
