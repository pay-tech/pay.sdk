package com.tec.pay.android.hybrid.model;

/**
 * GetResponse class.
 *
 * @author Lucas Cheung.
 * @date 2019-12-27.
 */
public class GetResponse {

  public String key;
  public Object value;

  public GetResponse(String key, Object value) {
    this.key = key;
    this.value = value;
  }
}
