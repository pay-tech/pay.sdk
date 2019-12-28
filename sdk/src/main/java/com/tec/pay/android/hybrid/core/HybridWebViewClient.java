package com.tec.pay.android.hybrid.core;

import android.annotation.TargetApi;
import android.graphics.Bitmap;
import android.os.Build;
import android.text.TextUtils;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebView;
import com.tec.pay.android.base.log.DLog;
import java.net.URL;
import java.util.Map;

@SuppressWarnings("deprecation")
class HybridWebViewClient extends BridgeWebViewClient {

  public static final String TAG = HybridWebViewClient.class.getSimpleName();

  private final HybridWebViewCore mParent;

  HybridWebViewClient(HybridWebViewCore parent) {
    super(parent);
    mParent = parent;
  }

  // Overrides

  @Override
  public boolean shouldOverrideUrlLoading(WebView view, String url) {
    boolean handled = super.shouldOverrideUrlLoading(view, url);
    if (handled) {
      return true;
    }
    DLog.v("onMessage[%s]", url);

    // ). do handle message
    return mParent.messageHandler().onWebMessage(mParent.parent(), url);
  }

  @TargetApi(Build.VERSION_CODES.LOLLIPOP)
  @Override
  public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
    return shouldOverrideUrlLoading(view, request.getUrl().toString());
  }

  @Override
  public void onPageStarted(WebView view, String url, Bitmap favicon) {
    super.onPageStarted(view, url, favicon);
    DLog.d(TAG, "onPageStarted [%s]", url);
    mParent.switchState(HybridWebViewCore.STATE_LOADING_STARTED);
    mParent.updateCurrentUrl(url);
  }

  @TargetApi(Build.VERSION_CODES.LOLLIPOP)
  @Override
  public void onPageFinished(WebView view, String url) {
    super.onPageFinished(view, url);
    DLog.d(TAG, "onPageFinished [%s]", url);
    final String currentUrl = mParent.currentUrl();
    if (!TextUtils.equals(currentUrl, url)) {
      DLog.w(TAG, "mismatch url between [%s] and [%s] in onPageFinished", currentUrl, url);
      mParent.updateCurrentUrl(url);
    }

    mParent.onPageFinished(url);
    mParent.switchState(HybridWebViewCore.STATE_LOADING_FINISHED);

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
      CookieManager.getInstance().flush();
    } else {
      CookieSyncManager.getInstance().sync();
    }
  }

  @Override
  public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
    if (!mParent.isBind()) {
      return;
    }
    DLog.v(TAG, "onLoadError[%d][%s][%s][%s][%s]", errorCode, description, failingUrl,
        mParent.originUrl(), mParent.currentUrl());
    try {
      URL origin = new URL(mParent.currentUrl());
      URL failed = new URL(failingUrl);
      if (!origin.getHost().equals(failed.getHost())) {
        return;
      }
    } catch (Exception e) {
      return;
    }
    if ((failingUrl == null
        && errorCode != -12) /*not bad url*/) { // when errorCode = -1 and net::ERR_CACHE_MISS
      return;
    }
    // disconnection
    if (errorCode == ERROR_HOST_LOOKUP || errorCode == ERROR_CONNECT || errorCode == -1) {
      mParent.client().onLoadError(mParent.parent(), errorCode, description, failingUrl);
      mParent.loadUri("about:blank");
    }
  }

  @TargetApi(Build.VERSION_CODES.M)
  @Override
  public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
    onReceivedError(
        view, error.getErrorCode(), error.getDescription().toString(), request.getUrl().toString());
  }

  @TargetApi(21)
  @Override
  public WebResourceResponse shouldInterceptRequest(WebView view, WebResourceRequest request) {
    if (request.isForMainFrame()) {
      return null;
    }
    // ). do handle request
    return internalInterceptRequest(
        mParent.parent(), request.getUrl().toString(), request.getRequestHeaders());
  }

  @Override
  public WebResourceResponse shouldInterceptRequest(WebView view, String url) {
    if (mParent.currentUrl().equals(url)) {
      return null;
    }
    return internalInterceptRequest(mParent.parent(), url, null);
  }

  private WebResourceResponse internalInterceptRequest(
      HybridWebView webView, String url, Map<String, String> requestHeaders) {
    DLog.v("onRequest[%s]", url);

    return mParent.requestHandler().onRequest(webView, url, requestHeaders);
  }
}
