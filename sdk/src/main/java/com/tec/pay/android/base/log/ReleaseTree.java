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

  private static final int MAX_LOG_LENGTH = 4000;
  private static final String DEFAULT_SECRET_TAG = "TEC";
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
    // open log
    // --> adb -d shell setprop log.tag.<secretTag> DEBUG
    // close log
    // --> adb -d shell setprop log.tag.<secretTag> INFO
    if (!Log.isLoggable(DEFAULT_SECRET_TAG, priority)) {
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
    if (message.length() < MAX_LOG_LENGTH) {
      if (priority == Log.ASSERT) {
        Log.wtf(tag, message);
      } else {
        Log.println(priority, tag, message);
      }
      return;
    }

    // Split by line, then ensure each line can fit into Log's maximum length.
    for (int i = 0, length = message.length(); i < length; i++) {
      int newline = message.indexOf('\n', i);
      newline = newline != -1 ? newline : length;
      do {
        int end = Math.min(newline, i + MAX_LOG_LENGTH);
        String part = message.substring(i, end);
        if (priority == Log.ASSERT) {
          Log.wtf(tag, part);
        } else {
          Log.println(priority, tag, part);
        }
        i = end;
      } while (i < newline);
    }
  }
}
