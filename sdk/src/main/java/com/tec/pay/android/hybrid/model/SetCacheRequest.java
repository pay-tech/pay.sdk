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
public class SetCacheRequest extends SetRequest {

  private long expiredTime;

  private SetCacheRequest(String key, String value, long expiredTime) {
    super(key, value);
    this.expiredTime = expiredTime;
  }

  public long getExpiredTime() {
    return expiredTime;
  }

  public static SetCacheRequest from(@NonNull JSONObject jsonObject) throws JSONException {
    return new SetCacheRequest(JsonUtils.getStringNonEmpty(jsonObject, "key"),
        JsonUtils.getStringNonEmpty(jsonObject, "value"), jsonObject.optLong("expiredTime", -1));
  }
}
