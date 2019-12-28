package com.tec.pay.android.hybrid.core;

import android.support.annotation.NonNull;
import com.tec.pay.android.base.log.DLog;
import com.tec.pay.android.hybrid.JsProtocolFactory;
import com.tec.pay.android.hybrid.model.Code;
import com.tec.pay.android.hybrid.presentation.IHybridClient;
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
    onSuccess(null);
  }

  public void onSuccess(Map<String, Object> data) {
    Code code;
    try {
      String json = JsProtocolFactory.responseSuccess(data);
      callBack.onCallBack(json);
      code = Code.SUCCESS;
    } catch (JSONException e) {
      code = Code.ERROR_JSON_PARSE;
      DLog.e(e);
    } catch (Throwable e) {
      code = Code.ERROR_UNKNOWN;
      DLog.e(e);
    }
    if (code.isSuccessful()) {
      return;
    }
    mClient.onLoadError(mParent, code.getCode(), code.getMsg(), "");
  }

  public void onError(Code errorCode) {
    onError(null, errorCode);
  }

  public void onError(Throwable e, Code errorCode) {
    Code code;
    try {
      if (e == null) {
        callBack.onCallBack(JsProtocolFactory.responseError(errorCode));
      } else {
        callBack.onCallBack(JsProtocolFactory.responseError(e, errorCode));
      }
      code = Code.SUCCESS;
    } catch (JSONException e2) {
      code = Code.ERROR_JSON_PARSE;
      DLog.e(e2);
    } catch (Throwable e2) {
      code = Code.ERROR_UNKNOWN;
      DLog.e(e2);
    }
    if (code.isSuccessful()) {
      return;
    }
    mClient.onLoadError(mParent, code.getCode(), code.getMsg(), "");
  }
}
