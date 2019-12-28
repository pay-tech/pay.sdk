package com.tec.pay.android.hybrid.core;

import android.support.annotation.NonNull;
import com.tec.pay.android.base.log.DLog;
import com.tec.pay.android.hybrid.HybridConstant;
import com.tec.pay.android.hybrid.IHybridObserver;
import com.tec.pay.android.hybrid.model.Code;
import com.tec.pay.android.hybrid.model.RequestBody;
import com.tec.pay.android.hybrid.presentation.IHybridClient;
import org.json.JSONException;


public class HybridMessageHandler implements BridgeHandler {

  private final IHybridObserver mObserver;
  private final IHybridClient mClient;
  private HybridWebView mParent;

  public HybridMessageHandler(@NonNull HybridWebView parent, @NonNull IHybridObserver observer
      , IHybridClient client) {
    mObserver = observer;
    mParent = parent;
    mClient = client;
  }

  /**
   * do handle the message from {@link android.webkit.WebView} via {@link
   * android.webkit.WebViewClient#shouldOverrideUrlLoading}
   *
   * @param view {@link HybridWebView} that send messages
   * @param url bridge message
   * @return True if the host application wants to leave the current {@link HybridWebView} and
   * handle the url itself, otherwise return false.
   */
  public boolean onWebMessage(HybridWebView view, String url) {
    return mObserver.onHttpUrl(view, url);
  }

  @Override
  public void handler(String data, CallBackFunction function) {
    BridgeCallback bridgeCallback = new BridgeCallback(mParent, mClient, function);
    try {
      onBridgeMessage(mParent, data, bridgeCallback);
    } catch (JSONException e) {
      bridgeCallback.onError(e, Code.ERROR_JSON_PARSE);
    } catch (Throwable e) {
      bridgeCallback.onError(e, Code.ERROR_UNKNOWN);
    }
  }

  public boolean onBridgeMessage(HybridWebView view, String msg, BridgeCallback function)
      throws JSONException {
    boolean handled = true;
    DLog.d("onBridge msg : %s", msg);

    RequestBody requestBody = RequestBody.from(msg);
    // ). notify downstream according |target|
    switch (requestBody.getService()) {
      case HybridConstant.SERVICE_UI:
        mObserver.onUiMessage(view, requestBody, function);
        break;
      case HybridConstant.SERVICE_CACHE:
        mObserver.onCacheMessage(view, requestBody, function);
        break;
      case HybridConstant.SERVICE_INFO:
        mObserver.onInfoMessage(view, requestBody, function);
        break;
      case HybridConstant.SERVICE_BIZ:
        mObserver.onBizMessage(view, requestBody, function);
        break;
      default:
        handled = false;
        break;
    }

    return handled;
  }
}
