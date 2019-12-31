package com.tec.pay.android.base.log;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import com.tec.pay.android.base.utils.AppUtil;

/**
 * ReleaseTree class.
 *
 * @author Lucas Cheung.
 * @date 2019-12-30.
 */
public class ReleaseTree extends DLog.Tree {

  private static final String TAG = "TecPay";
  private int priority;

  public ReleaseTree() {
    priority = Log.WARN;
  }

  public ReleaseTree(int priority) {
    this.priority = priority;
  }

  @Override
  protected boolean isLoggable(@Nullable String tag, int priority) {
    if (!AppUtil.isAppDebuggable()) {
      return false;
    }
    return priority >= this.priority;
  }

  @Nullable
  @Override
  String getTag() {
    return TAG;
  }

  @Override
  protected void log(int priority, @Nullable String tag, @NonNull String message,
      @Nullable Throwable t) {

  }
}
