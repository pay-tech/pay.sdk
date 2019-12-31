package com.tec.pay.android;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import com.tec.pay.android.base.data.BaseConstant;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * TecPayResult class.
 *
 * @author Lucas Cheung.
 * @date 2019-12-31.
 */
public class TecPayResult {

  private final int code;
  private final String msg;
  private Data data;

  public TecPayResult(int code, String msg) {
    this.code = code;
    this.msg = msg;
  }

  public TecPayResult setData(Data data) {
    this.data = data;
    return this;
  }

  public boolean isSuccessful() {
    return code == BaseConstant.CODE_SUCCESS;
  }

  public static TecPayResult makeSuccess() {
    return new TecPayResult(BaseConstant.CODE_SUCCESS, BaseConstant.MSG_SUCCESS);
  }

  public static TecPayResult makeSuccess(Data data) {
    return new TecPayResult(BaseConstant.CODE_SUCCESS, BaseConstant.MSG_SUCCESS).setData(data);
  }

  public static TecPayResult makeFailed(int code, Throwable e) {
    return new TecPayResult(code, e.getMessage());
  }

  public int getCode() {
    return code;
  }

  @NonNull
  public String getMsg() {
    return msg;
  }

  @Nullable
  public Data getData() {
    return data;
  }

  public static class Data {

    private final double amount;
    private final String currency;
    private final String orderId;
    private String passBack;

    public Data(double amount, String currency, String orderId) {
      this.amount = amount;
      this.currency = currency;
      this.orderId = orderId;
    }

    public static Data from(@Nullable JSONObject jsonObject) throws JSONException {
      if (jsonObject == null) {
        return null;
      }
      return new Data(jsonObject.getDouble("amount"), jsonObject.getString("currency"),
          jsonObject.getString("orderId")).setPassBack(jsonObject.optString("passBack"));
    }

    public Data setPassBack(String passBack) {
      this.passBack = passBack;
      return this;
    }

    public double getAmount() {
      return amount;
    }

    @NonNull
    public String getCurrency() {
      return currency;
    }

    @NonNull
    public String getOrderId() {
      return orderId;
    }

    @Nullable
    public String getPassBack() {
      return passBack;
    }
  }
}
