package com.tec.pay.android.hybrid;

import com.tec.pay.android.hybrid.core.HybridWebView;

/**
 * IHybridErrorHandler class.
 *
 * @author Lucas Cheung.
 * @date 2019-12-27.
 */
public interface IHybridErrorHandler {

  void handleError(HybridWebView webView, Throwable throwable);
}
