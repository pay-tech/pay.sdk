package com.tec.pay.android.base.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class NetUtils {

  private static NetworkInfo getAvailableNetworkInfo(Context context) {
    ConnectivityManager connectivity = null;
    try {
      connectivity = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
    } catch (Exception ignored) {
    }
    if (connectivity == null) {
      return null;
    }
    return connectivity.getActiveNetworkInfo();
  }

  public static boolean isNetworkAvailable(Context context) {
    try {
      NetworkInfo info = getAvailableNetworkInfo(context);
      return info != null && info.isConnected();
    } catch (Exception e) {
      return false;
    }
  }
}
