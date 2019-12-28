package com.tec.pay.android.hybrid.core;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.ContextWrapper;
import android.net.Uri;
import android.os.Build;
import android.os.Message;
import android.webkit.ConsoleMessage;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import com.tec.pay.android.base.log.DLog;
import com.tec.pay.android.filechooser.FileChooserUtil;
import com.tec.pay.android.hybrid.cookie.CookieHelper;

class HybridWebChromeClient extends WebChromeClient {

  private final HybridWebViewCore mParent;
  private FileChooserUtil mFileChooserUtil;

  HybridWebChromeClient(HybridWebViewCore parent) {
    mParent = parent;
  }

  // Overrides

  @Override
  public void onProgressChanged(WebView view, int newProgress) {
    if (!mParent.isBind()) {
      return;
    }
    DLog.i("onProgressChanged newProgress -> [%d]", newProgress);
    mParent.client().onProgressChanged(mParent.parent(), newProgress);
  }

  @Override
  public boolean onConsoleMessage(ConsoleMessage consoleMessage) {
    DLog.v("onConsoleMessage[%d][%s]", consoleMessage.lineNumber(), consoleMessage.message());
    return super.onConsoleMessage(consoleMessage);
  }

  @Override
  public boolean onCreateWindow(
      WebView view, boolean isDialog, boolean isUserGesture, Message resultMsg) {
    if (!mParent.isBind()) {
      return false;
    }
    Message href = view.getHandler().obtainMessage();
    view.requestFocusNodeHref(href);
    String url = href.getData().getString("url");
    DLog.d("onCreateWindow: " + url);

    CookieHelper.setAcceptThirdPartyCookies(view, true);
    mParent.client().onCreateWindow(mParent.parent(), isDialog, isUserGesture, resultMsg);
    return true;
  }

  @Override
  public void onCloseWindow(WebView window) {
    if (!mParent.isBind()) {
      return;
    }
    mParent.client().onCloseWindow(mParent.parent());
  }

  // For Android  >= 5.0
  @TargetApi(Build.VERSION_CODES.LOLLIPOP)
  @Override
  public boolean onShowFileChooser(
      WebView webView, ValueCallback<Uri[]> filePathCallback, FileChooserParams fileChooserParams) {
    DLog.d("onShowFileChooser");
    if (mParent.getContext() instanceof ContextWrapper) {
      ContextWrapper contextWrapper = (ContextWrapper) mParent.getContext();
      if (contextWrapper.getBaseContext() instanceof Activity) {
        if (mFileChooserUtil == null) {
          mFileChooserUtil = new FileChooserUtil((Activity) contextWrapper.getBaseContext());
        }
        mFileChooserUtil.showFileChooser(filePathCallback, fileChooserParams);
        return true;
      }
    }

    return false;
  }

  // For Android < 3.0
  public void openFileChooser(ValueCallback<Uri> uploadMsg) {
    this.openFileChooser(uploadMsg, "*/*");
  }

  // For Android >= 3.0
  public void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType) {
    this.openFileChooser(uploadMsg, acceptType, null);
  }

  // For Android >= 4.1
  public void openFileChooser(ValueCallback<Uri> uploadFile, String acceptType, String capture) {
    if (mParent.getContext() instanceof Activity) {
      if (mFileChooserUtil == null) {
        mFileChooserUtil = new FileChooserUtil((Activity) mParent.getContext());
      }
      mFileChooserUtil.showFileChooser(uploadFile, acceptType);
    }
  }
}
