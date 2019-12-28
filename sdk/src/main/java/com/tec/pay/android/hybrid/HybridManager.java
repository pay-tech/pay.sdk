package com.tec.pay.android.hybrid;

import android.content.Context;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import com.tec.pay.android.base.component.ContextManager;
import com.tec.pay.android.hybrid.presentation.IHybridClient;

/**
 * HybridManager class.
 *
 * @author Lucas Cheung.
 * @date 2019/4/13.
 */
public class HybridManager {

  private static final String TAG = "HybridManager";
  private final IHybridClient mDefaultHybridClient = new HybridClientImpl();
  private IHybridClient mCurrentHybridClient = mDefaultHybridClient;

  private HybridManager() {
    init(ContextManager.appContext());
    try {
      CookieManager cookieManager = CookieManager.getInstance();
      cookieManager.setAcceptCookie(true);
    } catch (Throwable ignored) {
    }
  }

  public static HybridManager instance() {
    return InstanceHolder.INSTANCE;
  }

  private static class InstanceHolder {

    private static final HybridManager INSTANCE = new HybridManager();
  }

  private void init(Context context) {
    if (context == null) {
      return;
    }
    CookieSyncManager.createInstance(context);
  }

  public void recycle() {
    WebViewPool.getInstance().clear();
  }
}
