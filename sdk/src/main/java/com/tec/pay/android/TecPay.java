package com.tec.pay.android;

import android.content.Context;
import android.support.annotation.NonNull;
import com.tec.pay.android.base.log.DLog;
import com.tec.pay.android.base.log.DLog.Tree;
import com.tec.pay.android.base.log.ReleaseTree;
import com.tec.pay.android.base.utils.Validator;
import com.tec.pay.android.controller.TecPayController;

/**
 * TecPay class.
 *
 * @author Lucas Cheung.
 * @date 2019-12-28.
 */
public class TecPay {

  private static Tree mReleaseTree;

  public static void init(Context context, String appId, String appKey) {
    TecPayController.instance().init(context, appId, appKey, null);
  }

  public static void init(Context context, String appId, String appKey, ActionCallback callback) {
    TecPayController.instance().init(context, appId, appKey, callback);
  }

  public static synchronized void setLoggerLevel(int priority) {
    if (mReleaseTree != null) {
      DLog.uproot(mReleaseTree);
    }
    mReleaseTree = new ReleaseTree(priority);
    DLog.plant(mReleaseTree);
  }

  public static void pay(@NonNull TecPayParam params, @NonNull TecPayCallback callback) {
    Validator.notNull(callback, "TecPayCallback");
    Validator.notNull(params, "TecPayParam");
    TecPayController.instance().pay(params, callback);
  }
}
