package com.tec.pay.android.hybrid.core;

import com.tec.pay.android.base.exception.TecException;
import java.util.Locale;

/**
 * HybridException class.
 *
 * @author Lucas Cheung.
 * @date 2019-06-11.
 */
public class HybridException extends TecException {

  private static final String ERROR = "Hybrid error, url -> [%s], code -> [%d]";

  public HybridException(String url, int code) {
    super(String.format(Locale.ENGLISH, ERROR, url, code), code);
  }

  public HybridException(Exception e, int code) {
    super(e, code);
  }
}
