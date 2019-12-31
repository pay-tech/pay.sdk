package com.tec.pay.android.hybrid.core;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.webkit.DownloadListener;
import android.webkit.URLUtil;
import com.tec.pay.android.base.log.DLog;
import com.tec.pay.android.hybrid.IHybridClient;
import com.tec.pay.android.hybrid.IHybridFactory;

public class HybridWebViewCore extends BridgeWebView {

  public static final String TAG = HybridWebViewCore.class.getSimpleName();

  public static final int STATE_IDLE = 0;
  public static final int STATE_LOAD_TRIGGERED = 1;
  public static final int STATE_LOADING_STARTED = 2;
  public static final int STATE_LOADING_FINISHED = 3;
  public static final int STATE_DESTROY = 4;

  private HybridWebView mParent;
  private IHybridClient mClient;

  // Init
  private HybridMessageHandler mMessageHandler;
  private HybridRequestHandler mRequestHandler;
  private String mOriginUrl;

  // Getter
  private String mCurrentUrl;
  private int mCurrentState = STATE_IDLE;
  private boolean isBind = false;

  public HybridWebViewCore(@NonNull Context context) {
    super(context);

    // setup
    setBackgroundColor(Color.WHITE);
    setFocusable(true);
    setFocusableInTouchMode(true);

    // download listener
    setDownloadListener(getDownloadListener());
    mOriginUrl = "";
    mCurrentUrl = "tec";
  }

  public void bind(@NonNull HybridWebView parent, @NonNull IHybridFactory factory) {
    mParent = parent;
    mClient = factory.client();
    mMessageHandler = new HybridMessageHandler(parent, factory.observer(), mClient);
    setDefaultHandler(mMessageHandler);
    mRequestHandler = new HybridRequestHandler(factory.router());
    // client
    setWebViewClient(new HybridWebViewClient(this));
    setWebChromeClient(new HybridWebChromeClient(this));
    isBind = true;
  }

  public void unBind() {
    isBind = false;
    try {
      super.setWebViewClient(null);
      super.setWebChromeClient(null);
    } catch (Exception ignored) {
    }
    mClient = null;
    mMessageHandler = null;
    mRequestHandler = null;
  }

  public boolean isBind() {
    return isBind;
  }

  protected DownloadListener getDownloadListener() {
    return new DownloadListener() {
      @Override
      public void onDownloadStart(
          String url,
          String userAgent,
          String contentDisposition,
          String mimeType,
          long contentLength) {
        final Intent intent = new Intent(Intent.ACTION_VIEW);
        final Uri uri = Uri.parse(url);
        intent.setData(uri);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        try {
          getContext().startActivity(intent);
        } catch (Exception e) {
          DLog.e("onDownloadStart error", e);
        }
      }
    };
  }

  public String originUrl() {
    return mOriginUrl;
  }

  // API

  public String currentUrl() {
    return mCurrentUrl;
  }

  HybridWebView parent() {
    return mParent;
  }

  // FSM

  IHybridClient client() {
    return mClient;
  }

  HybridMessageHandler messageHandler() {
    return mMessageHandler;
  }

  HybridRequestHandler requestHandler() {
    return mRequestHandler;
  }

  void updateCurrentUrl(String url) {
    if (!isBind) {
      return;
    }

    // ). do update url
    if (TextUtils.equals(mCurrentUrl, url)) {
      // multi-call onPageStarted with the same url?
      DLog.w(TAG, "multi-call onPageStarted with [%s]", url);
    } else {
      mCurrentUrl = url;
      mClient.onUpdateUrl(mParent, mCurrentUrl);
    }
  }

  public void loadUri(String uri) {
    if (!URLUtil.isValidUrl(uri)) {
      if (!isBind) {
        return;
      }
      mClient.onLoadError(mParent, -1, "url is error", uri);
      return;
    }
    DLog.i("start to load uri[%s]", uri);
    // ). try to switch state to |STATE_LOAD_TRIGGERED|
    switchState(STATE_LOAD_TRIGGERED);

    // ). check state
    switch (mCurrentState) {
      case STATE_LOAD_TRIGGERED:
        break;
      default:
        DLog.e(TAG, "should not load in state[%d]", mCurrentState);
        return;
    }

    // ). do load
    mOriginUrl = uri;
    loadUrl(uri);
    updateCurrentUrl(uri);
  }

  @Override
  public void destroy() {
    // Special handling:
    // Since |WebView.destroy()| is not a synchronized method, a quick open/close tab may
    // cause unwanted callback through |WebViewClient| and |WebChromeClient|.
    try {
      super.setWebViewClient(null);
      super.setWebChromeClient(null);
    } catch (Exception ignored) {
    }

    // ). change state to destroy
    switchState(STATE_DESTROY);

    // ). do destroy
    // In some special case, destroy |WebView| will trigger crash, so catch any exception here
    try {
      super.destroy();
    } catch (Exception e) {
      DLog.e(TAG, "exception during destroy [" + this + "]", e);
    }
  }

  void switchState(int toState) {
    switch (mCurrentState) {
      case STATE_IDLE:
        switch (toState) {
          case STATE_LOAD_TRIGGERED:
            mCurrentState = toState;
            break;
          default:
            break;
        }
        break;
      case STATE_LOAD_TRIGGERED:
        switch (toState) {
          case STATE_DESTROY:
            mCurrentState = toState;
            break;
          case STATE_LOADING_STARTED:
            mCurrentState = toState;
            onLoadStarted(currentUrl());
            break;
          case STATE_LOADING_FINISHED:
            mCurrentState = toState;
          default:
            break;
        }
        break;
      case STATE_LOADING_STARTED:
        switch (toState) {
          case STATE_DESTROY:
          case STATE_LOAD_TRIGGERED:
            DLog.i(TAG, "discard current loading[%s]", mCurrentUrl);
            stopLoading();
            onPageFinished(currentUrl());
            mCurrentState = STATE_LOAD_TRIGGERED;
            break;
          case STATE_LOADING_FINISHED:
            mCurrentState = toState;
            getSettings().setBlockNetworkImage(false);
            break;
          default:
            break;
        }
        break;
      case STATE_LOADING_FINISHED:
        switch (toState) {
          case STATE_DESTROY:
          case STATE_LOAD_TRIGGERED:
            mCurrentState = toState;
            break;
          case STATE_LOADING_STARTED:
            mCurrentState = toState;
            onLoadStarted(currentUrl());
            break;
          default:
            break;
        }
        break;
      default:
        break;
    }
    if (mCurrentState != toState) {
      DLog.w(TAG, "invalid switch state[%d]->[%d]", mCurrentState, toState);
    }
  }

  void onLoadStarted(String url) {
    if (!isBind) {
      return;
    }
    mClient.onLoadStarted(mParent);
  }

  void onPageFinished(String url) {
    if (!isBind) {
      return;
    }
    mClient.onLoadFinished(mParent);
  }
}
