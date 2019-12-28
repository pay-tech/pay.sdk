package com.tec.pay.android.hybrid.model;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import org.json.JSONException;
import org.json.JSONObject;

public class RequestBody {

  private String service;
  private String action;
  private JSONObject params;

  public RequestBody(String service, String action, JSONObject params) {
    this.service = service;
    this.action = action;
    this.params = params;
  }

  @NonNull
  public String getService() {
    return service;
  }

  @NonNull
  public String getAction() {
    return action;
  }

  @Nullable
  public JSONObject getParams() {
    return params;
  }

  public static RequestBody from(String json) throws JSONException {
    JSONObject jsonObject = new JSONObject(json);
    return new RequestBody(jsonObject.getString("service"), jsonObject.getString("action"),
        jsonObject.optJSONObject("params"));
  }
}
