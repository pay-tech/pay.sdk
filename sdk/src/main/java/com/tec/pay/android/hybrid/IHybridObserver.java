package com.tec.pay.android.hybrid;


import com.tec.pay.android.hybrid.core.BridgeCallback;
import com.tec.pay.android.hybrid.core.HybridWebView;
import com.tec.pay.android.hybrid.model.RequestBody;

public interface IHybridObserver {

  void onUiMessage(HybridWebView view, RequestBody requestBody, BridgeCallback function);

  void onCacheMessage(HybridWebView view, RequestBody requestBody, BridgeCallback function);

  void onInfoMessage(HybridWebView view, RequestBody requestBody, BridgeCallback function);

  void onBizMessage(HybridWebView view, RequestBody requestBody, BridgeCallback function);

  boolean onHttpUrl(HybridWebView view, String link);
}
