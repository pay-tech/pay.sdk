package com.tec.pay.android.hybrid.model;

import android.support.annotation.NonNull;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * GetArrayRequest class.
 *
 * @author Lucas Cheung.
 * @date 2020-01-10.
 */
public class GetArrayRequest {

  private List<GetRequest> getRequestList;

  public GetArrayRequest(List<GetRequest> getRequestList) {
    this.getRequestList = getRequestList;
  }

  public List<GetRequest> getGetRequestList() {
    return getRequestList;
  }

  public static GetArrayRequest from(@NonNull JSONObject jsonObject) throws JSONException {
    JSONArray keys = jsonObject.getJSONArray("keys");
    List<GetRequest> list = new ArrayList<>(keys.length());
    for (int i = 0; i < keys.length(); i++) {
      JSONObject item = keys.getJSONObject(i);
      list.add(GetRequest.from(item));
    }
    return new GetArrayRequest(list);
  }
}
