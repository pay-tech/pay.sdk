package com.tec.pay.android.hybrid.model;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * SetCacheRequest class.
 *
 * @author Lucas Cheung.
 * @date 2019-12-27.
 */
public class GetRequest {

  private String key;
  private String defValue;

  private GetRequest(String key, String defValue) {
    this.key = key;
    this.defValue = defValue;
  }

  @NonNull
  public String getKey() {
    return key;
  }

  @Nullable
  public String getDefValue() {
    return defValue;
  }

  public static GetRequest from(@NonNull JSONObject jsonObject) throws JSONException {
    return new GetRequest(jsonObject.getString("key"), jsonObject.optString("defValue"));
  }
}
