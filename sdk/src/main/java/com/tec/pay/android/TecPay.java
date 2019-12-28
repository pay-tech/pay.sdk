package com.tec.pay.android;

import android.content.Context;
import com.tec.pay.android.base.component.BaseHandler;
import com.tec.pay.android.base.component.ContextManager;
import com.tec.pay.android.base.log.DLog;
import com.tec.pay.android.base.log.DLog.DebugTree;

/**
 * TecPay class.
 *
 * @author Lucas Cheung.
 * @date 2019-12-28.
 */
public class TecPay {

  public static void init(Context context, String appId, String appKey) {
    ContextManager.init(context);
    BaseHandler.open();
    if (BuildConfig.DEBUG) {
      DLog.plant(new DebugTree());
    }
  }
}
