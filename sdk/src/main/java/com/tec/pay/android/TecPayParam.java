package com.tec.pay.android;

import android.net.Uri;
import android.support.annotation.Keep;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

/**
 * TecPayParam class.
 *
 * @author Lucas Cheung.
 * @date 2019-12-31.
 */
@Keep
public class TecPayParam {

  private final double amount;
  @NonNull
  private final String currency;
  @Nullable
  private final String methodId;
  @NonNull
  private final String orderId;
  @Nullable
  private final String passBack;
  @NonNull
  private final String goods;
  @NonNull
  private final String uid;

  private TecPayParam(Builder builder) {
    amount = builder.amount;
    currency = builder.currency;
    methodId = builder.methodId;
    orderId = builder.orderId;
    passBack = builder.passBack;
    goods = builder.goods;
    uid = builder.uid;
  }

  public double getAmount() {
    return amount;
  }

  @NonNull
  public String getCurrency() {
    return currency;
  }

  @Nullable
  public String getMethodId() {
    return methodId;
  }

  @NonNull
  public String getOrderId() {
    return orderId;
  }

  @Nullable
  public String getPassBack() {
    return passBack;
  }

  @NonNull
  public String getGoods() {
    return goods;
  }

  @NonNull
  public String getUid() {
    return uid;
  }

  public Uri.Builder handelUrl(Uri.Builder builder) {
    return builder.appendQueryParameter("amount", String.valueOf(amount))
        .appendQueryParameter("currency", currency)
        .appendQueryParameter("methodId", methodId)
        .appendQueryParameter("orderId", orderId)
        .appendQueryParameter("passBack", passBack)
        .appendQueryParameter("goods", goods)
        .appendQueryParameter("uid", uid);
  }

  public static Builder newBuilder() {
    return new Builder();
  }

  public static Builder builder(@NonNull TecPayParam tecPayParam) {
    return new Builder(tecPayParam);
  }

  @Keep
  public static final class Builder {

    private double amount;
    private String currency;
    private String methodId;
    private String orderId;
    private String passBack;
    private String goods;
    private String uid;

    private Builder() {
    }

    private Builder(TecPayParam param) {
      amount = param.amount;
      currency = param.currency;
      methodId = param.methodId;
      orderId = param.orderId;
      passBack = param.passBack;
      goods = param.goods;
      uid = param.uid;
    }

    public Builder amount(double amount) {
      this.amount = amount;
      return this;
    }

    public Builder currency(String currency) {
      this.currency = currency;
      return this;
    }

    public Builder methodId(String methodId) {
      this.methodId = methodId;
      return this;
    }

    public Builder orderId(String val) {
      orderId = val;
      return this;
    }

    public Builder passBack(String val) {
      passBack = val;
      return this;
    }

    public Builder goods(String val) {
      goods = val;
      return this;
    }

    public Builder uid(String uid) {
      this.uid = uid;
      return this;
    }

    public TecPayParam build() {
      return new TecPayParam(this);
    }
  }
}
