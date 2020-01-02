package com.tec.pay.android.hybrid.model;

import android.support.annotation.NonNull;
import com.tec.pay.android.base.utils.JsonUtils;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * SetCacheRequest class.
 *
 * @author Lucas Cheung.
 * @date 2019-12-27.
 */
public class SetRequest {

  private String key;
  private String value;

  SetRequest(String key, String value) {
    this.key = key;
    this.value = value;
  }

  @NonNull
  public String getKey() {
    return key;
  }

  @NonNull
  public String getValue() {
    return value;
  }


  public static SetRequest from(@NonNull JSONObject jsonObject) throws JSONException {
    return new SetRequest(JsonUtils.getStringNonEmpty(jsonObject, "key"),
        JsonUtils.getStringNonEmpty(jsonObject, "value"));
  }
}
