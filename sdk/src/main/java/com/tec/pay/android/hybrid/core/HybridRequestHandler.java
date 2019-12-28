package com.tec.pay.android.hybrid.core;

import android.support.annotation.NonNull;
import android.webkit.WebResourceResponse;
import com.tec.pay.android.hybrid.IHybridRouter;
import java.util.Map;

public class HybridRequestHandler {

  private final IHybridRouter mRouter;

  public HybridRequestHandler(@NonNull IHybridRouter hybridRouter) {
    mRouter = hybridRouter;
  }

  /**
   * do handle the request from {@link android.webkit.WebView} via {@link
   * android.webkit.WebViewClient#shouldInterceptRequest}
   *
   * @param view {@link HybridWebView} that send request
   * @param url request resource url
   * @return A {@link WebResourceResponse} containing the response information or null if the
   * WebView should load the resource itself.
   */
  public WebResourceResponse onRequest(HybridWebView view, String url,
      Map<String, String> requestHeaders) {
    // ). return response
    return mRouter.onWebRoute(view, url, requestHeaders);
  }
}
