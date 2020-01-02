package com.tec.pay.android.base.data;

/**
 * BaseConstant class.
 *
 * @author Lucas Cheung.
 * @date 2019-12-31.
 */
public class BaseConstant {

  public static final int CODE_SUCCESS = 0;
  public static final int CODE_ERROR_DEVELOPER = -2;
  public static final int CODE_PAY_CANCEL = -3;
  public static final int CODE_ERROR_JSON_PARSE = -10;
  public static final int CODE_ERROR_ACTION_GET_NULL = -11;
  public static final int CODE_ERROR_ACTION_NOT_BE_SUPPORT = -12;
  public static final int CODE_ERROR_SERVICE_NOT_BE_SUPPORT = -13;

  public static final String MSG_SUCCESS = "Success";
  public static final String MSG_DEVELOPER_ERROR = "Developer error: %s";
  public static final String MSG_ERROR_ACTION_GET_NULL = "Action get error: No value match for key %s";
  public static final String MSG_PAY_CANCEL = "Payment has been cancelled";
  public static final String MSG_ERROR_JSON_PARSE = "Json parse error: %s";
  public static final String MSG_ERROR_ACTION_NOT_BE_SUPPORT = "Hybrid action[%s] not be support.";
  public static final String MSG_ERROR_SERVICE_NOT_BE_SUPPORT = "Hybrid service[%s] not be support.";

}
