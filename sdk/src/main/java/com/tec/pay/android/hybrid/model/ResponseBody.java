package com.tec.pay.android.hybrid.model;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import com.tec.pay.android.base.utils.JsonUtils;
import com.tec.pay.android.base.utils.ObjectUtils;
import org.json.JSONException;
import org.json.JSONObject;

public class ResponseBody {

  /**
   * code : 0 msg : success data : {"value":"Toby"}
   */
  private int code;
  private String msg;
  private JSONObject data;

  public ResponseBody(int code, String msg, JSONObject data) {
    this.code = code;
    this.msg = msg;
    this.data = data;
  }

  public ResponseBody(int code, String msg) {
    this.code = code;
    this.msg = msg;
  }


  public int getCode() {
    return code;
  }

  @NonNull
  public String getMsg() {
    return msg;
  }

  @Nullable
  public JSONObject getData() {
    return data;
  }

  public static ResponseBody from(@NonNull JSONObject jsonObject) throws JSONException {
    return new ResponseBody(jsonObject.getInt("code"),
        JsonUtils.getStringNonEmpty(jsonObject, "msg"),
        jsonObject.optJSONObject("data"));
  }

  public String toJson() throws JSONException {
    JSONObject jsonObject = new JSONObject();
    jsonObject.put("code", code);
    jsonObject.put("msg", msg);
    if (!ObjectUtils.isEmpty(data)) {
      jsonObject.put("data", data);
    }
    return jsonObject.toString();
  }
}
