package com.tec.pay.android.hybrid.cookie;

import android.os.Build;
import android.webkit.CookieManager;
import android.webkit.WebView;

/**
 * CookieHelper class.
 *
 * @author Lucas Cheung.
 * @date 2019/3/19.
 */
public class CookieHelper {

  public static void setAcceptThirdPartyCookies(WebView webView, boolean accept) {
    CookieManager cookieManager = CookieManager.getInstance();

    // This is a safeguard, in case you've disabled cookies elsewhere
    if (accept && !cookieManager.acceptCookie()) {
      cookieManager.setAcceptCookie(true);
    }
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
      cookieManager.setAcceptThirdPartyCookies(webView, accept);
    }
  }
}
