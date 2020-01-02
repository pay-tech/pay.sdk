package com.tec.pay.android.hybrid;

import com.tec.pay.android.base.data.BaseConstant;
import com.tec.pay.android.base.exception.TecException;
import com.tec.pay.android.base.utils.TextHelper;
import com.tec.pay.android.hybrid.model.ResponseBody;
import org.json.JSONException;
import org.json.JSONObject;

public final class JsProtocolFactory {

  public static String responseSuccess() throws JSONException {
    return responseSuccess(null);
  }

  public static String responseSuccess(JSONObject data) throws JSONException {
    ResponseBody responseBody = new ResponseBody(BaseConstant.CODE_SUCCESS,
        BaseConstant.MSG_SUCCESS,
        data);
    return responseBody.toJson();
  }

  public static String responseError(Throwable e) throws JSONException {
    int errorCode;
    String errorMsg;
    if (e instanceof TecException) {
      errorCode = ((TecException) e).getCode();
      errorMsg = e.getMessage();
    } else {
      errorCode = BaseConstant.CODE_ERROR_DEVELOPER;
      errorMsg = String
          .format(BaseConstant.MSG_DEVELOPER_ERROR, TextHelper.ensureNotNull(e.getMessage()));
    }
    ResponseBody responseBody = new ResponseBody(errorCode, errorMsg);
    return responseBody.toJson();
  }

}
