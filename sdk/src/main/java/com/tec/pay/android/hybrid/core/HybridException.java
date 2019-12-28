package com.tec.pay.android.hybrid.core;

import java.util.Locale;

/**
 * HybridException class.
 *
 * @author Lucas Cheung.
 * @date 2019-06-11.
 */
public class HybridException extends Exception {

  private static final String ERROR = "Hybrid error, url -> [%s], code -> [%d]";
  private int code;

  public HybridException(String url, int code) {
    super(String.format(Locale.ENGLISH, ERROR, url, code));
    this.code = code;
  }

  public HybridException(Exception e, int code) {
    super(e);
    this.code = code;
  }

  public int getCode() {
    return code;
  }
}
