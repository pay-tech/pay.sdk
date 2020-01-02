package com.tec.pay.android.controller;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import com.tec.pay.android.BuildConfig;
import com.tec.pay.android.TecPayCallback;
import com.tec.pay.android.TecPayParam;
import com.tec.pay.android.TecPayResult;
import com.tec.pay.android.base.component.BaseHandler;
import com.tec.pay.android.base.component.ContextManager;
import com.tec.pay.android.base.data.BaseConstant;
import com.tec.pay.android.base.log.DLog;
import com.tec.pay.android.base.log.DLog.DebugTree;
import com.tec.pay.android.base.utils.Validator;
import com.tec.pay.android.task.Task;
import java.util.HashSet;
import java.util.Set;

/**
 * TecPayController class.
 *
 * @author Lucas Cheung.
 * @date 2019-12-30.
 */
public class TecPayController {

  private static final int STATE_IDLE = 0;
  private static final int STATE_RUNNING = 1;
  private static final int STATE_SUCCESS = 2;

  private final Set<TecPayCallback> mObservers = new HashSet<>();
  private volatile int mCurrentState = STATE_IDLE;
  private String mAppId;
  private String mAppKey;

  private TecPayController() {
  }

  public static TecPayController instance() {
    return InstanceHolder.INSTANCE;
  }

  private static class InstanceHolder {

    private static final TecPayController INSTANCE = new TecPayController();
  }

  public synchronized void init(Context context, String appId, String appKey,
      TecPayCallback callback) {
    try {
      Validator.notNull(context, "context");
      Validator.notEmpty(appId, "appId");
      Validator.notEmpty(appKey, "appKey");
    } catch (Exception e) {
      callback.onResult(TecPayResult.makeFailed(BaseConstant.CODE_ERROR_DEVELOPER, e));
      return;
    }
    if (callback != null) {
      mObservers.add(callback);
    }
    if (isSdkInitialized()) {
      notifyObserver(null);
      return;
    }
    if (mCurrentState == STATE_RUNNING) {
      return;
    }
    switchState(STATE_RUNNING);
    ContextManager.init(context);
    BaseHandler.open();
    if (BuildConfig.DEBUG) {
      DLog.plant(new DebugTree());
    }
    mAppId = appId;
    mAppKey = appKey;
    initInternal(appId, appKey).continueWith(task -> {
      if (task.isFaulted()) {
        switchState(STATE_IDLE);
      } else {
        switchState(STATE_SUCCESS);
      }
      notifyObserver(task.getError());
      return null;
    }, Task.UI_THREAD_EXECUTOR);
  }

  public final boolean isSdkInitialized() {
    return mCurrentState == STATE_SUCCESS;
  }

  private Task<Void> initInternal(String appId, String appKey) {
    return Task.call(() -> {
      HybridController.instance().init(ContextManager.appContext(), appId, appKey);
      return null;
    }, Task.BACKGROUND_EXECUTOR);
  }

  private synchronized void notifyObserver(final Exception error) {
    for (TecPayCallback observer : mObservers) {
      if (error == null) {
        observer.onResult(TecPayResult.makeSuccess());
      } else {
        observer.onResult(TecPayResult.makeFailed(BaseConstant.CODE_ERROR_DEVELOPER, error));
      }
    }
    mObservers.clear();
  }

  private void switchState(int state) {
    mCurrentState = state;
  }

  private TecPayCallback mTecPayCallback;

  public void pay(TecPayParam params, @NonNull TecPayCallback result) {
    if (!isSdkInitialized()) {
      return;
    }
    mTecPayCallback = result;

    try {
      Uri.Builder uriBuilder = Uri.parse("http://192.168.166.23:8080").buildUpon();
//      Uri.Builder uriBuilder = Uri.parse("file:///android_asset/demo.html").buildUpon();
      uriBuilder.appendQueryParameter("appId", mAppId).appendQueryParameter("appKey", mAppKey);
      params.handelUrl(uriBuilder);
      HybridController.instance().showWeb(uriBuilder.toString());
    } catch (Exception e) {
      DLog.e(e);
      result.onResult(TecPayResult.makeFailed(BaseConstant.CODE_ERROR_DEVELOPER, e));
    }
  }

  boolean payComplete(TecPayResult result) {
    if (!isSdkInitialized()) {
      return false;
    }
    mTecPayCallback.onResult(result);
    return true;
  }
}
