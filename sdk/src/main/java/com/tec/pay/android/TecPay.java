package com.tec.pay.android;

import android.content.Context;
import android.support.annotation.Keep;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
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
@Keep
public class TecPay {

  private static Tree sReleaseLogTree;

  public static void init(@NonNull Context context, @NonNull String appId, @NonNull String appKey) {
    TecPayController.instance().init(context, appId, appKey, null);
  }

  public static void init(@NonNull Context context, @NonNull String appId, @NonNull String appKey,
      @Nullable TecPayCallback callback) {
    TecPayController.instance().init(context, appId, appKey, callback);
  }

  public static synchronized void setLoggerLevel(int priority) {
    if (sReleaseLogTree != null) {
      DLog.uproot(sReleaseLogTree);
    }
    sReleaseLogTree = new ReleaseTree(priority);
    DLog.plant(sReleaseLogTree);
  }

  public static void pay(@NonNull TecPayParam params, @NonNull TecPayCallback callback) {
    Validator.notNull(callback, "TecPayCallback");
    Validator.notNull(params, "TecPayParam");
    TecPayController.instance().pay(params, callback);
  }
}
