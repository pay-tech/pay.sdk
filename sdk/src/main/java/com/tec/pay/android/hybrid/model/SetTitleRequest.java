package com.tec.pay.android.hybrid.model;

import android.support.annotation.NonNull;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * SetTitleRequest class.
 *
 * @author Lucas Cheung.
 * @date 2019-12-30.
 */
public class SetTitleRequest {

  private String title;

  SetTitleRequest(String title) {
    this.title = title;
  }

  @NonNull
  public String getTitle() {
    return title;
  }

  public static SetTitleRequest from(@NonNull JSONObject jsonObject) throws JSONException {
    return new SetTitleRequest(jsonObject.getString("title"));
  }
}
