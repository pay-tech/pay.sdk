package com.tec.pay.android.hybrid;

import android.support.annotation.Nullable;
import android.webkit.WebResourceResponse;
import com.tec.pay.android.hybrid.core.HybridWebView;
import java.util.Map;

public interface IHybridRouter {

  WebResourceResponse onWebRoute(HybridWebView view, String url,
      @Nullable Map<String, String> requestHeaders);
}