package com.tec.pay.android.hybrid.model;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import com.tec.pay.android.base.utils.JsonUtils;
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
    String key;
    String defValue;
    key = JsonUtils.getStringNonEmpty(jsonObject, "key");
    defValue = jsonObject.optString("defValue");
    return new GetRequest(key, defValue);
  }
}
