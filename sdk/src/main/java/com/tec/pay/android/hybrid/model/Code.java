package com.tec.pay.android.hybrid.model;

import com.tec.pay.android.hybrid.HybridConstant;

public enum Code {
  SUCCESS(HybridConstant.MSG_SUCCESS, HybridConstant.CODE_SUCCESS),
  ERROR_UNKNOWN("unknown error", -1),
  ERROR_EMPTY_PARAM("param can not be empty", -2),
  ERROR_ACTION_NOT_BE_SUPPORT("action not be support", -21),
  ERROR_JSON_PARSE("json parse error", -10),
  ERROR_DEVELOPER("developer error", -11);

  private String msg;
  private int code;

  Code(String msg, int code) {
    this.msg = msg;
    this.code = code;
  }

  public String getMsg() {
    return msg;
  }

  public int getCode() {
    return code;
  }

  public boolean isSuccessful() {
    return HybridConstant.CODE_SUCCESS == getCode();
  }
}
