package com.tec.pay.android.hybrid.core;

import android.support.annotation.NonNull;
import com.tec.pay.android.base.exception.JSONParseException;
import com.tec.pay.android.base.log.DLog;
import com.tec.pay.android.base.utils.JsonUtils;
import com.tec.pay.android.hybrid.IHybridClient;
import com.tec.pay.android.hybrid.JsProtocolFactory;
import java.util.Collections;
import java.util.Map;
import org.json.JSONException;

/**
 * BridgeCallback class.
 *
 * @author Lucas Cheung.
 * @date 2019-12-27.
 */
public class BridgeCallback {

  private CallBackFunction callBack;
  private final IHybridClient mClient;
  private HybridWebView mParent;

  public BridgeCallback(@NonNull HybridWebView parent, IHybridClient client,
      @NonNull CallBackFunction callBack) {
    this.callBack = callBack;
    mClient = client;
    mParent = parent;
  }

  public void onSuccess() {
    onSuccess(Collections.emptyMap());
  }

  public void onSuccess(@NonNull Map<String, Object> data) {
    try {
      String json = JsProtocolFactory.responseSuccess(JsonUtils.toJSONObject(data));
      callBack.onCallBack(json);
    } catch (Throwable e) {
      DLog.e(e);
    }
  }

  public void onError(Throwable e) {
    try {
      final Throwable throwable;
      if (e instanceof JSONException) {
        throwable = new JSONParseException((JSONException) e);
      } else {
        throwable = e;
      }
      callBack.onCallBack(JsProtocolFactory.responseError(throwable));
      DLog.w(e.toString());
    } catch (Throwable e2) {
      DLog.e(e2);
    }
  }
}
