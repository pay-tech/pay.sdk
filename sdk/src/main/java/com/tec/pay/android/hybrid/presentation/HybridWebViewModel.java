package com.tec.pay.android.hybrid.presentation;

import android.os.Message;
import android.support.annotation.Nullable;
import android.webkit.WebResourceResponse;
import com.tec.pay.android.base.component.ContextManager;
import com.tec.pay.android.base.log.DLog;
import com.tec.pay.android.base.utils.Validator;
import com.tec.pay.android.hybrid.HybridConstant;
import com.tec.pay.android.hybrid.IHybridObserver;
import com.tec.pay.android.hybrid.IHybridRouter;
import com.tec.pay.android.hybrid.core.BridgeCallback;
import com.tec.pay.android.hybrid.core.HybridWebView;
import com.tec.pay.android.hybrid.data.HybridDataManager;
import com.tec.pay.android.hybrid.model.Code;
import com.tec.pay.android.hybrid.model.GetResponse;
import com.tec.pay.android.hybrid.model.RequestBody;
import com.tec.pay.android.hybrid.model.cache.GetCacheRequest;
import com.tec.pay.android.hybrid.model.cache.SetCacheRequest;
import com.tec.pay.android.task.Task;
import java.util.Collections;
import java.util.Map;
import org.json.JSONObject;

/**
 * HybridWebViewModel class.
 *
 * @author Lucas Cheung.
 * @date 2019-12-27.
 */
public class HybridWebViewModel implements IHybridClient, IHybridRouter, IHybridObserver {

  private HybridDataManager mHybridDataManager;

  public HybridWebViewModel(HybridDataManager hybridDataManager) {
    mHybridDataManager = hybridDataManager;
  }

  @Override
  public void onUiMessage(HybridWebView view, RequestBody requestBody, BridgeCallback function) {
    switch (requestBody.getAction()) {
      case HybridConstant.ACTION_CLOSE:
        if (ContextManager.activity() != null) {
          ContextManager.activity().finish();
        }
        break;
      case HybridConstant.ACTION_SET_TITLE:
        break;
      default:
    }
  }

  @Override
  public void onCacheMessage(HybridWebView view, RequestBody requestBody, BridgeCallback function) {

    switch (requestBody.getAction()) {
      case HybridConstant.ACTION_GET:
        Task.forResult(requestBody.getParams()).continueWith(task -> {
          JSONObject params = task.getResult();
          Validator.notNull(params, "params");
          return GetCacheRequest.from(params);
        }, Task.BACKGROUND_EXECUTOR).onSuccessTask(task -> {
          GetCacheRequest result = task.getResult();
          return mHybridDataManager.getCache(result.getKey(), result.getDefValue());
        }).continueWith(task -> {
          if (task.isFaulted()) {
            Exception error = task.getError();
            function.onError(error, Code.ERROR_DEVELOPER);
          } else {
            GetResponse result = task.getResult();
            function.onSuccess(Collections.singletonMap(result.key, result.value));
          }
          return null;
        }, Task.UI_THREAD_EXECUTOR);

        break;
      case HybridConstant.ACTION_SET:
        Task.forResult(requestBody.getParams()).continueWith(task -> {
          JSONObject params = task.getResult();
          Validator.notNull(params, "params");
          return SetCacheRequest.from(params);
        }, Task.BACKGROUND_EXECUTOR).onSuccessTask(task -> {
          SetCacheRequest result = task.getResult();
          return mHybridDataManager
              .setCache(result.getKey(), result.getValue(), result.getExpiredTime());
        }).continueWith(task -> {
          if (task.isFaulted()) {
            Exception error = task.getError();
            function.onError(error, Code.ERROR_DEVELOPER);
          } else {
            function.onSuccess();
          }
          return null;
        }, Task.UI_THREAD_EXECUTOR);
        break;
      case HybridConstant.ACTION_FLUSH:
        Task.callInBackground(() -> mHybridDataManager.flushCache()).continueWith(task -> {
          if (task.isFaulted()) {
            Exception error = task.getError();
            function.onError(error, Code.ERROR_DEVELOPER);
          } else {
            function.onSuccess();
          }
          return null;
        }, Task.UI_THREAD_EXECUTOR);
        break;
      case HybridConstant.ACTION_DEL:
        Task.forResult(requestBody.getParams()).continueWith(task -> {
          JSONObject params = task.getResult();
          Validator.notNull(params, "params");
          return params.getString("key");
        }, Task.BACKGROUND_EXECUTOR).onSuccessTask(task -> {
          return mHybridDataManager.delCache(task.getResult());
        }).continueWith(task -> {
          if (task.isFaulted()) {
            Exception error = task.getError();
            function.onError(error, Code.ERROR_DEVELOPER);
          } else {
            function.onSuccess();
          }
          return null;
        }, Task.UI_THREAD_EXECUTOR);
        break;
      default:
        function.onError(Code.ERROR_ACTION_NOT_BE_SUPPORT);
    }
  }

  @Override
  public void onInfoMessage(HybridWebView view, RequestBody requestBody, BridgeCallback function) {
    switch (requestBody.getAction()) {
      case HybridConstant.ACTION_GET:
        break;
      default:
    }
  }

  @Override
  public void onBizMessage(HybridWebView view, RequestBody requestBody, BridgeCallback function) {
    switch (requestBody.getAction()) {
      case HybridConstant.ACTION_CALLBACK:
        break;
      default:
    }
  }

  @Override
  public boolean onHttpUrl(HybridWebView view, String link) {
    return false;
  }

  @Override
  public WebResourceResponse onWebRoute(HybridWebView view, String url,
      @Nullable Map<String, String> requestHeaders) {
    return null;
  }

  @Override
  public void onLoadStarted(HybridWebView view) {

  }

  @Override
  public void onLoadFinished(HybridWebView view) {

  }

  @Override
  public void onUpdateUrl(HybridWebView view, String url) {

  }

  @Override
  public void onUpdateTitle(HybridWebView view, String title) {

  }

  @Override
  public void onLoadError(HybridWebView view, int errorCode, String errorMessage,
      String failingUrl) {
    DLog.e(
        "onLoadError errorMessage -> [%s], failingUrl -> [%s] , code -> [%d]",
        errorMessage, failingUrl, errorCode);
  }

  @Override
  public void onProgressChanged(HybridWebView parent, int newProgress) {

  }

  @Override
  public void onCreateWindow(HybridWebView view, boolean isDialog, boolean isUserGesture,
      Message webViewMessage) {

  }

  @Override
  public void onCloseWindow(HybridWebView view) {

  }

  @Override
  public void onCloseClient() {

  }
}
