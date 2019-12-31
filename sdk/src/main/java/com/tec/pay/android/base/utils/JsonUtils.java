package com.tec.pay.android.base.utils;

import android.support.annotation.NonNull;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * JsonUtils class.
 *
 * @author Lucas Cheung.
 * @date 2019-12-31.
 */
public class JsonUtils {

  public static Map<String, Object> toMap(JSONObject jsonObject) throws JSONException {
    if (jsonObject == null) {
      return Collections.emptyMap();
    }
    Map<String, Object> data = new HashMap<>();
    Iterator<String> it = jsonObject.keys();
    while (it.hasNext()) {
      String key = it.next();
      data.put(key, jsonObject.get(key));
    }
    return data;
  }

  public static JSONObject toJSONObject(@NonNull Map<String, Object> map) {
    return new JSONObject(map);
  }
}
