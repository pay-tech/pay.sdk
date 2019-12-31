package com.tec.pay.android.hybrid;

import com.tec.pay.android.base.utils.TextHelper;
import com.tec.pay.android.hybrid.model.Code;
import com.tec.pay.android.hybrid.model.ResponseBody;
import org.json.JSONException;
import org.json.JSONObject;

public final class JsProtocolFactory {

  public static String responseSuccess() throws JSONException {
    return responseSuccess(null);
  }

  public static String responseSuccess(JSONObject data) throws JSONException {
    ResponseBody responseBody = new ResponseBody(Code.SUCCESS.getCode(), Code.SUCCESS.getMsg(),
        data);
    return responseBody.toJson();
  }

  public static String responseError(Throwable e, Code code) throws JSONException {
    ResponseBody responseBody = new ResponseBody(code.getCode(),
        String.format("%s: %s", code.getMsg(),
            TextHelper.ensureNotNull(e.getMessage())));
    return responseBody.toJson();
  }

  public static String responseError(Code code) throws JSONException {
    ResponseBody responseBody = new ResponseBody(code.getCode(), code.getMsg());
    return responseBody.toJson();
  }

}
