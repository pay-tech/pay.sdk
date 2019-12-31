package com.tec.pay.android.controller;

import android.content.Context;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import com.tec.pay.android.TecPayResult;
import com.tec.pay.android.TecPayResult.Data;
import com.tec.pay.android.base.component.ContextManager;
import com.tec.pay.android.base.data.BaseConstant;
import com.tec.pay.android.hybrid.WebViewPool;
import com.tec.pay.android.hybrid.data.HybridDataManager;
import com.tec.pay.android.hybrid.model.ResponseBody;
import com.tec.pay.android.hybrid.presentation.main.TecPayActivity;
import com.tec.pay.android.task.Task;

/**
 * HybridController class.
 *
 * @author Lucas Cheung.
 * @date 2019/4/13.
 */
public class HybridController {

  private static final int STATE_IDLE = 0;
  private static final int STATE_PAYING = 1;

  private volatile int mCurrentState = STATE_IDLE;

  private HybridController() {
  }

  public static HybridController instance() {
    return InstanceHolder.INSTANCE;
  }

  private static class InstanceHolder {

    private static final HybridController INSTANCE = new HybridController();
  }

  public void init(Context context, String appId, String appKey) {
    if (context == null) {
      return;
    }
    CookieSyncManager.createInstance(context);
    try {
      CookieManager cookieManager = CookieManager.getInstance();
      cookieManager.setAcceptCookie(true);
    } catch (Throwable ignored) {
    }
    HybridDataManager.instance().init(appId, appKey);
  }

  void showWeb(String url) {
    switchState(STATE_PAYING);
    TecPayActivity.show(ContextManager.appContext(), url);
  }

  public Task<Boolean> handlePayComplete(ResponseBody result) {
    return Task.call(() -> {
      TecPayController.instance()
          .payComplete(new TecPayResult(result.getCode(), result.getMsg()).setData(
              Data.from(result.getData())));
      switchState(STATE_IDLE);
      return true;
    });
  }

  public void handleHybridClose() {
    if (mCurrentState == STATE_IDLE) {
      return;
    }
    TecPayController.instance().payComplete(
        new TecPayResult(BaseConstant.CODE_PAY_CANCEL, BaseConstant.MSG_PAY_CANCEL));
  }

  public void recycle() {
    WebViewPool.getInstance().clear();
  }

  private void switchState(int state) {
    mCurrentState = state;
  }
}
