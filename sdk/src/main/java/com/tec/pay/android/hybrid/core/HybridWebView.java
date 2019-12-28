package com.tec.pay.android.hybrid.core;

import android.content.Context;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.webkit.WebBackForwardList;
import android.webkit.WebHistoryItem;
import android.webkit.WebView.WebViewTransport;
import android.widget.FrameLayout;
import com.tec.pay.android.base.log.DLog;
import com.tec.pay.android.hybrid.IHybridFactory;
import com.tec.pay.android.hybrid.cookie.CookieHelper;
import java.util.UUID;

public class HybridWebView extends FrameLayout {

  public static final String TAG = HybridWebView.class.getSimpleName();

  private HybridWebViewCore mCore;
  @Nullable
  private Message mWebViewMessage;

  public HybridWebView(@NonNull Context context) {
    this(context, null);
  }

  public HybridWebView(@NonNull Context context, @Nullable AttributeSet attrs) {
    this(context, attrs, 0);
  }

  public HybridWebView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
    setTag(UUID.randomUUID().toString());
  }

  /**
   * go back ,it will skill error tips page
   *
   * @param errorUrl error tips page's url
   * @return if can go back
   */
  public boolean goBack(@NonNull String errorUrl) {
    if (!isAvailable()) {
      return false;
    }
    int step = goBackIndex(errorUrl);
    if (mCore.canGoBackOrForward(step)) {
      mCore.goBackOrForward(step);
      return true;
    }
    return false;
  }

  /**
   * go forward ,it will skill error tips page
   *
   * @param errorUrl error tips page's url
   * @return if can go forward
   */
  public boolean goForward(@NonNull String errorUrl) {
    if (!isAvailable()) {
      return false;
    }
    int step = goForwardIndex(errorUrl);
    if (mCore.canGoBackOrForward(step)) {
      mCore.goBackOrForward(step);
      return true;
    }
    return false;
  }

  /**
   * @param errorUrl error tips page's url
   * @return number of steps back which skip error tips page's url
   */
  public int goBackIndex(String errorUrl) {
    if (!isAvailable()) {
      return 0;
    }
    WebBackForwardList list = mCore.copyBackForwardList();
    int currentIndex = list.getCurrentIndex();
    int index = currentIndex;
    WebHistoryItem item = list.getItemAtIndex(index);
    while (item != null && errorUrl.equals(item.getUrl())) {
      item = list.getItemAtIndex(--index);
    }
    return index - currentIndex - 1;
  }

  /**
   * @param errorUrl error tips page's url
   * @return number of steps forward which skip error tips page's url
   */
  public int goForwardIndex(String errorUrl) {
    if (!isAvailable()) {
      return 0;
    }
    WebBackForwardList list = mCore.copyBackForwardList();
    int currentIndex = list.getCurrentIndex();
    int index = currentIndex + 1;
    WebHistoryItem item = list.getItemAtIndex(index);
    while (item != null && errorUrl.equals(item.getUrl())) {
      item = list.getItemAtIndex(++index);
    }
    return index - currentIndex;
  }

  // API

  public void init() {
    if (mCore != null) {
      return;
    }
    try {
      // webview core
      mCore = new HybridWebViewCore(getContext());
      addView(mCore, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
      CookieHelper.setAcceptThirdPartyCookies(mCore, true);
    } catch (Throwable e) {
      DLog.e(TAG, "HybridWebView init error", e);
//      StatisticHelper.reportError(e);
    }
  }

  public void bind(IHybridFactory hybridFactory) {
    if (mCore == null) {
      return;
    }
    mCore.bind(this, hybridFactory);
  }

  public void unBind() {
    if (mCore == null) {
      return;
    }
    mCore.unBind();
  }

  public void loadUri(String uri) {
    if (!isAvailable()) {
      return;
    }
    mCore.loadUri(uri);
  }

  public void loadDataWithBaseURL(
      String baseUrl, String data, String mimeType, String encoding, String failUrl) {
    if (!isAvailable()) {
      return;
    }
    mCore.loadDataWithBaseURL(baseUrl, data, mimeType, encoding, failUrl);
  }

  public void reload() {
    if (!isAvailable()) {
      return;
    }
    mCore.reload();
  }

  public boolean canGoBack() {
    if (isAvailable()) {
      return mCore.canGoBack();
    } else {
      return false;
    }
  }

  public void goBackOrForward(int steps) {
    if (!isAvailable()) {
      return;
    }
    mCore.goBackOrForward(steps);
  }

  public boolean canGoBackOrForward(int steps) {
    if (!isAvailable()) {
      return false;
    }
    return mCore.canGoBackOrForward(steps);
  }

  public void goBack() {
    if (!isAvailable()) {
      return;
    }
    String currentUrl = mCore.getUrl();
  }

  public String originUrl() {
    if (!isAvailable()) {
      return "";
    }
    return mCore.originUrl();
  }

  public String currentUrl() {
    if (!isAvailable()) {
      return "";
    }
    return mCore.currentUrl();
  }

  public boolean canGoForward() {
    if (!isAvailable()) {
      return false;
    }
    return mCore.canGoForward();
  }

  public void goForward() {
    if (!isAvailable()) {
      return;
    }
    mCore.goForward();
  }

  public void clearHistory() {
    if (!isAvailable()) {
      return;
    }
    mCore.clearHistory();
  }

  public WebBackForwardList copyBackForwardList() {
    return mCore.copyBackForwardList();
  }

  public int getWebViewScrollY() {
    return mCore.getScrollY();
  }
  // Lifecycle

  public void setWebViewMessage(Message webViewMessage) {
    if (webViewMessage == null) {
      return;
    }
    if (webViewMessage.obj == null) {
      return;
    }
    if (webViewMessage.obj instanceof WebViewTransport) {
      this.mWebViewMessage = webViewMessage;
      ((WebViewTransport) webViewMessage.obj).setWebView(mCore);
    }
  }

  @Override
  protected void onAttachedToWindow() {
    super.onAttachedToWindow();
    if (mWebViewMessage != null) {
      mWebViewMessage.sendToTarget();
    }
  }

  public void onPause() {
    if (!isAvailable()) {
      return;
    }
    mCore.onPause();
  }

  public void onResume() {
    if (!isAvailable()) {
      return;
    }
    mCore.onResume();
    mCore.requestFocus();
  }

  public void destroy() {
    onPause();
    removeAllViews();
    if (mCore != null) {
      mCore.stopLoading();
      mCore.getSettings().setJavaScriptEnabled(false);
      mCore.clearHistory();
      mCore.clearView();
      mCore.removeAllViews();
      mCore.destroy();
    }
  }

  public boolean isAvailable() {
    return mCore != null && mCore.isBind();
  }
}
