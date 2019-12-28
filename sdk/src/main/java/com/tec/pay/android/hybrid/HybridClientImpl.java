package com.tec.pay.android.hybrid;

import android.os.Message;
import com.tec.pay.android.hybrid.core.HybridWebView;
import com.tec.pay.android.hybrid.presentation.IHybridClient;

/**
 * HybridClientImpl class.
 *
 * @author Lucas Cheung.
 * @date 2019/4/16.
 */
public class HybridClientImpl implements IHybridClient {

  @Override
  public void onLoadStarted(HybridWebView view) {
  }

  @Override
  public void onLoadFinished(HybridWebView view) {
  }

  @Override
  public void onUpdateUrl(HybridWebView view, String url) {
  }

  @Override
  public void onUpdateTitle(HybridWebView view, String title) {
  }

  @Override
  public void onLoadError(
      HybridWebView view, int errorCode, String errorMessage, String failingUrl) {
  }

  @Override
  public void onProgressChanged(HybridWebView parent, int newProgress) {
  }

  @Override
  public void onCreateWindow(
      HybridWebView view, boolean isDialog, boolean isUserGesture, Message webViewMessage) {
  }

  @Override
  public void onCloseWindow(HybridWebView view) {
  }

  @Override
  public void onCloseClient() {
  }
}
