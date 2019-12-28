package com.tec.pay.android.hybrid.model.cache;

import android.support.annotation.NonNull;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * SetCacheRequest class.
 *
 * @author Lucas Cheung.
 * @date 2019-12-27.
 */
public class SetCacheRequest {

  private String key;
  private String value;
  private long expiredTime;

  private SetCacheRequest(String key, String value, long expiredTime) {
    this.key = key;
    this.value = value;
    this.expiredTime = expiredTime;
  }

  @NonNull
  public String getKey() {
    return key;
  }

  @NonNull
  public String getValue() {
    return value;
  }

  public long getExpiredTime() {
    return expiredTime;
  }

  public static SetCacheRequest from(@NonNull JSONObject jsonObject) throws JSONException {
    return new SetCacheRequest(jsonObject.getString("key"),
        jsonObject.getString("value"), jsonObject.optLong("expiredTime", -1));
  }
}
