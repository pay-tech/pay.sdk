package com.tec.pay.android.base.exception;

/**
 * HybridException class.
 *
 * @author Lucas Cheung.
 * @date 2019-06-11.
 */
public class TecException extends Exception {

  private int code;

  public TecException(String msg, int code) {
    super(msg);
    this.code = code;
  }

  public TecException(Exception e, int code) {
    super(e);
    this.code = code;
  }

  public int getCode() {
    return code;
  }

  @Override
  public String toString() {
    String s = getClass().getName();
    String message = getLocalizedMessage();
    return (message != null) ? String.format(s + ":[%s][%s]", getCode(), message) : s;
  }
}
