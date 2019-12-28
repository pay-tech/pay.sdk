package com.tec.pay.android.hybrid.presentation;

import android.os.Message;
import com.tec.pay.android.hybrid.core.HybridWebView;

public interface IHybridClient {

  void onLoadStarted(HybridWebView view);

  void onLoadFinished(HybridWebView view);

  void onUpdateUrl(HybridWebView view, String url);

  void onUpdateTitle(HybridWebView view, String title);

  void onLoadError(HybridWebView view, int errorCode, String errorMessage, String failingUrl);

  void onProgressChanged(HybridWebView parent, int newProgress);

  void onCreateWindow(
      HybridWebView view, boolean isDialog, boolean isUserGesture, Message webViewMessage);

  void onCloseWindow(HybridWebView view);

  void onCloseClient();
}
