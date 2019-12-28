package com.tec.pay.android.hybrid.model;

import com.tec.pay.android.base.utils.ObjectUtils;
import java.util.Map;
import org.json.JSONException;
import org.json.JSONObject;

public class ResponseBody {

  /**
   * code : 0 msg : success data : {"value":"Toby"}
   */
  private int code;
  private String msg;
  private Map data;

  public ResponseBody(int code, String msg, Map data) {
    this.code = code;
    this.msg = msg;
    this.data = data;
  }

  public ResponseBody(int code, String msg) {
    this.code = code;
    this.msg = msg;
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
